/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import org.martin.cloudClient.gui.model.RenderListDirs;
import org.martin.cloudClient.gui.model.TCRFiles;
import org.martin.cloudClient.gui.model.TMFiles;
import org.martin.cloudClient.model.LMDirectories;
import org.martin.cloudClient.model.PBModel;
import org.martin.cloudClient.net.Connector;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.model.packages.UpdatePackage;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudCommon.system.Archive;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.Folder;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudCommon.system.Utilities;

/**
 *
 * @author martin
 */
public class GUICloudClient extends javax.swing.JFrame {

    private static final byte ENTER_KEY = KeyEvent.VK_ENTER;
    public Connector connector;
    private Socket socket;
    public ClientPackage cliPackage;
    private static GUICloudClient gui;
    //private final ImageIcon fileImg;
    public JFileChooser fileChoos;
    private static final byte optionSi = JOptionPane.YES_OPTION;
    private static final NumberFormat nf = new DecimalFormat("#0.00");
    
    public static void newInstance(){
        gui = new GUICloudClient();
        gui.setVisible(true);
    }
    
    public static GUICloudClient getInstance(){
        return gui;
    }
    
    public enum For{
        DIRECTORIES, FILES, ALL;
    }
    
    public GUICloudClient() {
        /*this.fileImg = new ImageIcon(
                getClass()
                        .getResource("/org/martin/cloudClient/gui/icons/file 48x48"));
        */
        initComponents();
        fileChoos = new JFileChooser();
        fileChoos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChoos.setMultiSelectionEnabled(true);
        fileChoos.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return "Solo archivos";
            }
        });
        setLocationRelativeTo(null);
        setResizable(false);
        formClientManagement.setDefaultCloseOperation(EXIT_ON_CLOSE);
        formClientRegister.setSize(formClientRegister.getPreferredSize());
        formClientRegister.setResizable(false);
        dialogNewFolder.setSize(380, 50);
        dialogNewFolder.setResizable(false);
        dialogFileOptions.setSize(577, 120);
        dialogFileOptions.setResizable(false);
        dialogFileOptions.setLocationRelativeTo(null);
        dialogFolderOptions.setSize(577, 120);
        dialogFolderOptions.setResizable(false);
        dialogFolderOptions.setLocationRelativeTo(null);
        dialogRename.setSize(dialogRename.getPreferredSize());
        dialogRename.setResizable(false);
        dialogRename.setLocationRelativeTo(null);
        dialogUploadOptions.setResizable(false);
        dialogUploadOptions.setLocationRelativeTo(null);
        dialogUploadOptions.setSize(397, 110);
        spaceBar.setString(spaceBar.getValue() + "MB");
        spaceBar.setStringPainted(true);
        tblFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Investigar SwingWorker --> permite la ejecucion de procesos que requieren
        // tiempo mediante un proceso en segundo plano para que la gui no se cuelgue
    }

    public ClientPackage getCliPackage() {
        return cliPackage;
    }
    
    private void openWindow(Window window, Component objectiveLocation){
        window.setLocationRelativeTo(objectiveLocation);
        window.show();
    }
    
    public void openWindow(Window window, Component objectiveLocation, int width, 
            int height){
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.show();
    }

    public void openWindow(Window window, Component objetiveLocation, Dimension dimension){
        window.setSize(dimension);
        window.setLocationRelativeTo(objetiveLocation);
        window.show();
    }
    
    private void closeSession() {
        try {
            connector.sendCommand(Command.close);
            connector.closeConnection();
            connector = null;
            lblUserName.setText(null);
            formClientManagement.setVisible(false);
            setVisible(true);
            txtNick.requestFocus();
            JOptionPane.showMessageDialog(this, "Sesión cerrada exitosamente");
        } catch (IOException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File getSelectedDir(){
        return new File(cliPackage.getCurrentDir(), listDirectories.getSelectedValue());
    }

    private File getFileOnTable(int index){
       return ((TMFiles)tblFiles.getModel()).getSelectedFile(index);
    }
    
    private File getSelectedFileOnTable(){
       final int index = tblFiles.getSelectedRow();
       return ((TMFiles)tblFiles.getModel()).getSelectedFile(index);
    }
    
    private File[] getSelectedFilesOnTable(){
        final int[] selectedRows = tblFiles.getSelectedRows();
        System.out.println("Selected rows: "+Arrays.toString(selectedRows));
        final int lenRows = selectedRows.length;
        final File[] selectedFiles = new File[lenRows];
        
        for (int i = 0; i < lenRows; i++) 
            selectedFiles[i] = getFileOnTable(selectedRows[i]);
        
        return selectedFiles;
    }
    
    private void deleteSelectedFiles() throws IOException{
        final File[] files = getSelectedFilesOnTable();
        for (File file : files){
            System.out.println("Selected file: "+file.getCanonicalPath());
            deleteFile(file.getCanonicalPath());
        }
    }
    
    private void downloadFile() throws IOException, ClassNotFoundException{
        downloadFile(getSelectedFileOnTable());
    }
    
    private void configFileChooser(For FOR){
        fileChoos.setMultiSelectionEnabled(!(FOR == For.DIRECTORIES));
        switch(FOR){
            case DIRECTORIES:
                fileChoos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                break;
            case FILES:
                fileChoos.setFileSelectionMode(JFileChooser.FILES_ONLY);
                break;
            
            case ALL:
                fileChoos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                break;
        }
        
    }
    
    private void downloadFile(File file) throws IOException, ClassNotFoundException{
        configFileChooser(For.FILES);
        fileChoos.showSaveDialog(formClientManagement);
        File localPath = fileChoos.getSelectedFile();
        
        if (localPath != null) {
            
            final Command cmd = new Command("@dwnF");
            cmd.addOption(localPath.getCanonicalPath());
            cmd.addOption(file.getCanonicalPath());
            connector.sendCommand(cmd);
            final Archive downloaded = (Archive) connector.getReceivedObject();
            downloaded.create();
            JOptionPane.showMessageDialog(formClientManagement, "Archivo descargado correctamente\n"
                    + "en la carpeta "+localPath.getParentFile().getName());
        }
        else
            JOptionPane.showMessageDialog(formClientManagement, 
                    "No se ha seleccionado ninguna ubicación", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    private void downloadFolder(File file){
        
    }
    
    private void download(File file) throws IOException, ClassNotFoundException{
        if(file.isDirectory())
            downloadFolder(file);
        else
            downloadFile(file);
    }
    
    public long getTotalSize(File[] files){
        if(files == null) return 0;
        if(files.length == 0) return 0;
        
        long totalSize = 0;
        for (File file : files) {
            if (file.isDirectory())
                totalSize+=getTotalSize(file.listFiles());
            else
                totalSize+=file.length();
        }
        
        return totalSize;
    }
    
    private void renameFile(){
        final String newName = txtNewNameFile.getText().trim();
        if (!newName.isEmpty()) {
            try {
                final Command cmdRename = new Command("@rename");
                cmdRename.addOption(getSelectedFileOnTable().getCanonicalPath());
                cmdRename.addOption(newName);
                connector.sendCommand(cmdRename);
                connector.sendUpdateRequest(cliPackage.getCurrentDirPath(), 
                        cliPackage.getUserNick());
                cliPackage.update((UpdatePackage) connector.getReceivedObject());
                updateTable();
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteFile(String path) throws IOException{
        final Command cmdDelF = new Command(Command.delF.getOrder(), path);
        System.out.println("CMD Delete file: "+cmdDelF);
        connector.sendCommand(cmdDelF);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formClientManagement = new javax.swing.JFrame();
        panelInfo = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spaceBar = new javax.swing.JProgressBar();
        lblUserName = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        panelContent = new javax.swing.JPanel();
        panelDirectories = new javax.swing.JPanel();
        listFIles = new javax.swing.JScrollPane();
        listDirectories = new javax.swing.JList<>();
        btnFolderOptions = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelFiles = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();
        panelIcons = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnUpdateDirectory = new javax.swing.JButton();
        btnUploadFile = new javax.swing.JButton();
        btnAddFolder = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        cboOrderOption = new javax.swing.JComboBox<>();
        cboOrderType = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnOrderFiles = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnDeleteSelectedFIles = new javax.swing.JButton();
        lblCurrentDir = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        itemInfoAccount = new javax.swing.JMenuItem();
        formClientRegister = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtRegNick = new javax.swing.JTextField();
        txtRegPass1 = new javax.swing.JPasswordField();
        txtRegPass2 = new javax.swing.JPasswordField();
        btnRegUser = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dialogNewFolder = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtNewFolderName = new javax.swing.JTextField();
        dialogUserInfo = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblUsedSpace = new javax.swing.JLabel();
        lblFreeSpace = new javax.swing.JLabel();
        lblTotalSpace = new javax.swing.JLabel();
        lblCreationDate = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        dialogUploadOptions = new javax.swing.JDialog();
        panelUploadOptions = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        btnFolderOption = new javax.swing.JButton();
        btnFileOption = new javax.swing.JButton();
        btnZipOption = new javax.swing.JButton();
        dialogFileOptions = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btnDeleteFile = new javax.swing.JButton();
        btnRenameFile = new javax.swing.JButton();
        btnDownloadFile = new javax.swing.JButton();
        btnGoBackFile = new javax.swing.JButton();
        dialogRename = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        txtNewNameFile = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        dialogFolderOptions = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        btnDeleteFile1 = new javax.swing.JButton();
        btnRenameFile1 = new javax.swing.JButton();
        btnDownloadFile1 = new javax.swing.JButton();
        btnGoBackFile1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();

        formClientManagement.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formClientManagementWindowClosing(evt);
            }
        });

        panelInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 255, 153), null));

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Nombre de Usuario: ");

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Espacio Utilizado: ");

        lblUserName.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblUserName.setText(" ");

        jLabel17.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel17.setText(" ");

        javax.swing.GroupLayout panelInfoLayout = new javax.swing.GroupLayout(panelInfo);
        panelInfo.setLayout(panelInfoLayout);
        panelInfoLayout.setHorizontalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spaceBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelInfoLayout.setVerticalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(lblUserName)))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spaceBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelDirectories.setBackground(java.awt.Color.white);
        panelDirectories.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 255, 51), null), "Directorios"));

        listDirectories.setBackground(new java.awt.Color(153, 255, 153));
        listDirectories.setModel(new DefaultListModel<String>()
        );
        listDirectories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listDirectoriesMouseReleased(evt);
            }
        });
        listFIles.setViewportView(listDirectories);

        btnFolderOptions.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnFolderOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/folder.png"))); // NOI18N
        btnFolderOptions.setText("Opciones");
        btnFolderOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderOptionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDirectoriesLayout = new javax.swing.GroupLayout(panelDirectories);
        panelDirectories.setLayout(panelDirectoriesLayout);
        panelDirectoriesLayout.setHorizontalGroup(
            panelDirectoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnFolderOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(listFIles, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelDirectoriesLayout.setVerticalGroup(
            panelDirectoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDirectoriesLayout.createSequentialGroup()
                .addComponent(btnFolderOptions)
                .addGap(0, 0, 0)
                .addComponent(listFIles, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelFiles.setBackground(new java.awt.Color(204, 255, 204));
        panelFiles.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 255, 102), null), "Archivos"));
        panelFiles.setLayout(new java.awt.BorderLayout());

        tblFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblFilesMouseReleased(evt);
            }
        });
        tblFiles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblFilesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblFiles);

        panelFiles.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(panelFiles);

        javax.swing.GroupLayout panelContentLayout = new javax.swing.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDirectories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(panelDirectories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnBack.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/goBack.png"))); // NOI18N
        btnBack.setToolTipText("Volver Atras");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/home.png"))); // NOI18N
        btnHome.setToolTipText("Volver a raiz");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnUpdateDirectory.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnUpdateDirectory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/update.png"))); // NOI18N
        btnUpdateDirectory.setToolTipText("Actualizar");
        btnUpdateDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDirectoryActionPerformed(evt);
            }
        });

        btnUploadFile.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnUploadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/upload.png"))); // NOI18N
        btnUploadFile.setToolTipText("Subir");
        btnUploadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadFileActionPerformed(evt);
            }
        });

        btnAddFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/newFolder.png"))); // NOI18N
        btnAddFolder.setToolTipText("Nueva Carpeta");
        btnAddFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFolderActionPerformed(evt);
            }
        });

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/search2.png"))); // NOI18N
        btnSearch.setToolTipText("Buscar archivos y/o directorios");
        btnSearch.setEnabled(false);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        cboOrderOption.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cboOrderOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Tamaño", "Fecha", "Tipo", "Formato" }));
        cboOrderOption.setEnabled(false);

        cboOrderType.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cboOrderType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascendente", "Descendente" }));
        cboOrderType.setEnabled(false);

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel14.setText("Orden: ");
        jLabel14.setEnabled(false);

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel13.setText("Ordenar elementos por: ");
        jLabel13.setEnabled(false);

        btnOrderFiles.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnOrderFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/orderFiles.png"))); // NOI18N
        btnOrderFiles.setToolTipText("Ordenar");
        btnOrderFiles.setEnabled(false);
        btnOrderFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrderFilesActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/exit.png"))); // NOI18N
        jButton4.setToolTipText("Cerrar Sesion");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnDeleteSelectedFIles.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnDeleteSelectedFIles.setText("Eliminar Seleccionados");
        btnDeleteSelectedFIles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedFIlesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelIconsLayout = new javax.swing.GroupLayout(panelIcons);
        panelIcons.setLayout(panelIconsLayout);
        panelIconsLayout.setHorizontalGroup(
            panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIconsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelIconsLayout.createSequentialGroup()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdateDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUploadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelIconsLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboOrderOption, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnOrderFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelIconsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelIconsLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnDeleteSelectedFIles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelIconsLayout.setVerticalGroup(
            panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIconsLayout.createSequentialGroup()
                .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelIconsLayout.createSequentialGroup()
                        .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUploadFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdateDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddFolder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelIconsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(cboOrderOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)
                                .addComponent(cboOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnDeleteSelectedFIles, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(btnOrderFiles, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        lblCurrentDir.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblCurrentDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/folder.png"))); // NOI18N
        lblCurrentDir.setText("Directorio Actual: ");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Info");

        itemInfoAccount.setText("Información de la Cuenta");
        itemInfoAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemInfoAccountActionPerformed(evt);
            }
        });
        jMenu2.add(itemInfoAccount);

        jMenuBar1.add(jMenu2);

        formClientManagement.setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout formClientManagementLayout = new javax.swing.GroupLayout(formClientManagement.getContentPane());
        formClientManagement.getContentPane().setLayout(formClientManagementLayout);
        formClientManagementLayout.setHorizontalGroup(
            formClientManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formClientManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formClientManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formClientManagementLayout.createSequentialGroup()
                        .addGroup(formClientManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelIcons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4))))
            .addGroup(formClientManagementLayout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(lblCurrentDir, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        formClientManagementLayout.setVerticalGroup(
            formClientManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formClientManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelIcons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(lblCurrentDir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 255), new java.awt.Color(153, 255, 153)));

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel10.setText("Nick: ");

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel11.setText("Contraseña: ");

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel12.setText("Repita la contraseña: ");

        txtRegPass1.setText("jPasswordField1");

        txtRegPass2.setText("jPasswordField1");

        btnRegUser.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnRegUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/registerUser1 32x32.png"))); // NOI18N
        btnRegUser.setText("Registrarse");
        btnRegUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegUser, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(txtRegNick)
                    .addComponent(txtRegPass1)
                    .addComponent(txtRegPass2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtRegNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtRegPass1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtRegPass2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Registro de Usuarios");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/cloud2 128x128.png"))); // NOI18N
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 255, 153), null));

        javax.swing.GroupLayout formClientRegisterLayout = new javax.swing.GroupLayout(formClientRegister.getContentPane());
        formClientRegister.getContentPane().setLayout(formClientRegisterLayout);
        formClientRegisterLayout.setHorizontalGroup(
            formClientRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formClientRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formClientRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formClientRegisterLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        formClientRegisterLayout.setVerticalGroup(
            formClientRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formClientRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(formClientRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        dialogNewFolder.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                dialogNewFolderWindowOpened(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel16.setText("Nombre de la carpeta: ");

        txtNewFolderName.setToolTipText("Escriba el nombre de la nueva carpeta, al finalizar solo pulse ENTER.");
        txtNewFolderName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNewFolderNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNewFolderName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtNewFolderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        dialogNewFolder.getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Información de la Cuenta"));

        jLabel18.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel18.setText("Espacio Utilizado: ");

        jLabel19.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel19.setText("Espacio Total: ");

        jLabel20.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel20.setText("Espacio Disponible: ");

        jLabel21.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel21.setText("Fecha de creación: ");

        lblUsedSpace.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblUsedSpace.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsedSpace.setText(" ");
        lblUsedSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        lblFreeSpace.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblFreeSpace.setForeground(java.awt.Color.green);
        lblFreeSpace.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFreeSpace.setText(" ");
        lblFreeSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        lblTotalSpace.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblTotalSpace.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSpace.setText(" ");
        lblTotalSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        lblCreationDate.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblCreationDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCreationDate.setText(" ");
        lblCreationDate.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsedSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCreationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFreeSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblUsedSpace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblFreeSpace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(lblTotalSpace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblCreationDate))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Purisa", 1, 36)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Electro Cloud");
        jLabel22.setOpaque(true);

        javax.swing.GroupLayout dialogUserInfoLayout = new javax.swing.GroupLayout(dialogUserInfo.getContentPane());
        dialogUserInfo.getContentPane().setLayout(dialogUserInfoLayout);
        dialogUserInfoLayout.setHorizontalGroup(
            dialogUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dialogUserInfoLayout.setVerticalGroup(
            dialogUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogUserInfoLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
        );

        dialogUploadOptions.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                dialogUploadOptionsWindowOpened(evt);
            }
        });

        panelUploadOptions.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 255, 102), null));

        jLabel23.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("¿Que desea subir?");

        btnFolderOption.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnFolderOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/folder.png"))); // NOI18N
        btnFolderOption.setText("Carpetas");
        btnFolderOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderOptionActionPerformed(evt);
            }
        });

        btnFileOption.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/file.png"))); // NOI18N
        btnFileOption.setText("Archivos");
        btnFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileOptionActionPerformed(evt);
            }
        });

        btnZipOption.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnZipOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/zipFile.png"))); // NOI18N
        btnZipOption.setText("ZIP");
        btnZipOption.setEnabled(false);
        btnZipOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZipOptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUploadOptionsLayout = new javax.swing.GroupLayout(panelUploadOptions);
        panelUploadOptions.setLayout(panelUploadOptionsLayout);
        panelUploadOptionsLayout.setHorizontalGroup(
            panelUploadOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUploadOptionsLayout.createSequentialGroup()
                .addGroup(panelUploadOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUploadOptionsLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelUploadOptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnFileOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFolderOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnZipOption)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUploadOptionsLayout.setVerticalGroup(
            panelUploadOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUploadOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addGroup(panelUploadOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFileOption)
                    .addComponent(btnFolderOption)
                    .addComponent(btnZipOption))
                .addContainerGap())
        );

        dialogUploadOptions.getContentPane().add(panelUploadOptions, java.awt.BorderLayout.CENTER);

        dialogFileOptions.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                dialogFileOptionsWindowOpened(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 255), null));

        jLabel24.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("¿Que desea hacer?");

        btnDeleteFile.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnDeleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/removeFile1.png"))); // NOI18N
        btnDeleteFile.setText("Eliminar");
        btnDeleteFile.setToolTipText("Eliminar");
        btnDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFileActionPerformed(evt);
            }
        });

        btnRenameFile.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnRenameFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/renameFile.png"))); // NOI18N
        btnRenameFile.setText("Renombrar");
        btnRenameFile.setToolTipText("Renombrar");
        btnRenameFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameFileActionPerformed(evt);
            }
        });

        btnDownloadFile.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/download.png"))); // NOI18N
        btnDownloadFile.setText("Descargar");
        btnDownloadFile.setToolTipText("Descargar");
        btnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadFileActionPerformed(evt);
            }
        });

        btnGoBackFile.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnGoBackFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/goBack.png"))); // NOI18N
        btnGoBackFile.setText("Regresar");
        btnGoBackFile.setToolTipText("Regresar");
        btnGoBackFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeleteFile)
                .addGap(18, 18, 18)
                .addComponent(btnRenameFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDownloadFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGoBackFile)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRenameFile)
                    .addComponent(btnDeleteFile)
                    .addComponent(btnDownloadFile)
                    .addComponent(btnGoBackFile))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialogFileOptions.getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        txtNewNameFile.setToolTipText("Escriba el nuevo nombre que tendrá el archivo. Al terminar solo presione ENTER");
        txtNewNameFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNewNameFileKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel25.setText("Nuevo nombre: ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNewNameFile, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtNewNameFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        dialogRename.getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        dialogFolderOptions.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                dialogFolderOptionsWindowOpened(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 255), null));

        jLabel26.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("¿Que desea hacer?");

        btnDeleteFile1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnDeleteFile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/removeFile1.png"))); // NOI18N
        btnDeleteFile1.setText("Eliminar");
        btnDeleteFile1.setToolTipText("Eliminar");
        btnDeleteFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFile1ActionPerformed(evt);
            }
        });

        btnRenameFile1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnRenameFile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/renameFile.png"))); // NOI18N
        btnRenameFile1.setText("Renombrar");
        btnRenameFile1.setToolTipText("Renombrar");
        btnRenameFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameFile1ActionPerformed(evt);
            }
        });

        btnDownloadFile1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnDownloadFile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/download.png"))); // NOI18N
        btnDownloadFile1.setText("Descargar");
        btnDownloadFile1.setToolTipText("Descargar");
        btnDownloadFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadFile1ActionPerformed(evt);
            }
        });

        btnGoBackFile1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        btnGoBackFile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/goBack.png"))); // NOI18N
        btnGoBackFile1.setText("Regresar");
        btnGoBackFile1.setToolTipText("Regresar");
        btnGoBackFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackFile1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeleteFile1)
                .addGap(18, 18, 18)
                .addComponent(btnRenameFile1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDownloadFile1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGoBackFile1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRenameFile1)
                    .addComponent(btnDeleteFile1)
                    .addComponent(btnDownloadFile1)
                    .addComponent(btnGoBackFile1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialogFolderOptions.getContentPane().add(jPanel7, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 255, 102), null));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/cloudClient.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel3.setText("Usuario: ");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel4.setText("Contraseña: ");

        txtNick.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNickKeyReleased(evt);
            }
        });

        txtPassword.setText("jPasswordField1");
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnLogin.setText("Ingresar");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel5.setText("¿No tienes cuenta?");

        jButton2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jButton2.setText("Regístrate aquí");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPassword)
                                    .addComponent(txtNick))
                                .addGap(29, 29, 29))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnLogin)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addGap(0, 66, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jButton2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Bienvenido a ElectroCloud");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            btnGoBackFile.setEnabled(false);
            // Para el @back el addOption funciona bien pero se debe revisar para
            // otras operaciones
            Command cmd = new Command("@back");
            cmd.addOption(cliPackage.getCurrentDir().getCanonicalPath());
            connector.sendCommand(cmd);
            Object objReceived = connector.getReceivedObject();
            cliPackage.setCurrentDir((File) objReceived);
            updateAll();
            System.out.println("Carpeta raiz despues de @back: "+cliPackage.getCurrentDir());
            btnGoBackFile.setEnabled(true);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        try {
            Command cmd = new Command("@root");
            connector.sendCommand(cmd);
            Object objReceived = connector.getReceivedObject();
            cliPackage.setCurrentDir((File) objReceived);
            updateAll();
            System.out.println("Carpeta raiz despues de @root: "+cliPackage.getCurrentDir());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnUpdateDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDirectoryActionPerformed
        try {
            connector.sendUpdateRequest(cliPackage.getCurrentDirPath(), cliPackage.getUserNick());
            cliPackage.update(connector.getUpdatesReceived());
            updateAll();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdateDirectoryActionPerformed

    private void btnUploadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadFileActionPerformed
        fileChoos.showOpenDialog(formClientManagement);
        final File[] selectedFiles = fileChoos.getSelectedFiles();
        
        if (selectedFiles != null) {
            try {
                String localPath, remotePath;
                Command cmd;
                Archive archive;
                Object objReceived;
                
                final boolean hasSufficientSpace = getTotalSize(selectedFiles) <= cliPackage.getAccount().getFreeSpace();
                
                if (hasSufficientSpace) {
                    for (File selectedFile : selectedFiles) {
                        localPath = selectedFile.getCanonicalPath();
                        remotePath = cliPackage.getCurrentDir().getCanonicalPath();
                
                        cmd = new Command("@uplF", localPath, remotePath);
                        archive = new Archive(remotePath, selectedFile.getName());
                        archive.writeBytesFrom(selectedFile);
                
                        connector.sendTransferPackage(new TransferPackage(cmd, archive));
                        connector.sendCommand(new Command("@update",
                                cliPackage.getCurrentDir().getCanonicalPath(), 
                                cliPackage.getUserNick()));
                
                        objReceived = connector.getReceivedObject();
                        cliPackage.update((UpdatePackage) objReceived);
                        System.out.println("Account: "+((UpdatePackage)objReceived).getAccount());
                        System.out.println("CurrentDir: "+((UpdatePackage)objReceived).getCurrentDir());
                        updateAll();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(formClientManagement, 
                            "No hay espacio suficiente para subir archivos\n"
                                    + "Espacio disponible: "+cliPackage.getFreeSpaceInMB() + "MB", 
                            "Mensaje", JOptionPane.WARNING_MESSAGE);
                }
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_btnUploadFileActionPerformed
    private void btnAddFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFolderActionPerformed
        dialogNewFolder.setLocationRelativeTo(formClientManagement);
        dialogNewFolder.setVisible(true);
    }//GEN-LAST:event_btnAddFolderActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnOrderFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrderFilesActionPerformed
    }//GEN-LAST:event_btnOrderFilesActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        int option = JOptionPane.showConfirmDialog(formClientManagement, 
                "¿Esta seguro que desea cerrar sesión?", "Confirmación", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (option == optionSi) closeSession();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnRegUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegUserActionPerformed
        final String user = txtRegNick.getText();
        final String pass1 = txtRegPass1.getText();
        final String pass2 = txtRegPass2.getText();
        
        if (user.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            JOptionPane.showMessageDialog(formClientRegister, "Por favor rellene todos los campos");
            txtRegNick.selectAll();
            txtRegNick.requestFocus();
        }
        else if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(formClientRegister, "Las contraseñas no coinciden");
            txtRegPass1.selectAll();
            txtRegPass1.requestFocus();
        }
        else{
            try {
                // Esto se debe cambiar
                connector = new Connector(SysInfo.LOCALHOST, SysInfo.DEFAULT_PORT);
                connector.sendCommand(Command.newRegU(user, pass1));
                final Object objReceived = connector.getReceivedObject();
                if (objReceived instanceof Boolean)
                    if ((Boolean)objReceived){
                        JOptionPane.showMessageDialog(formClientRegister, "Usuario registrado exitosamente",
                                "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        txtRegNick.setText(null);
                        txtRegPass1.setText(null);
                        txtRegPass2.setText(null);
                        formClientRegister.setVisible(false);
                        
                        txtNick.setText(user);
                        txtPassword.setText(pass1);
                        txtNick.selectAll();
                        txtNick.requestFocus();
                    }
                    else{
                        JOptionPane.showMessageDialog(formClientRegister, 
                                "El nick no se encuentra disponible",
                                "Mensaje", JOptionPane.WARNING_MESSAGE);
                        txtRegNick.selectAll();
                        txtRegNick.requestFocus();
                    }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnRegUserActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        openWindow(formClientRegister, this);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String nick = txtNick.getText();
        String passw = txtPassword.getText();
        
        if (nick.isEmpty() || passw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor rellene todos los campos");
            txtNick.selectAll();
            txtNick.requestFocus();
        }
        else{
            try {
                UserPackage up;
                connector = new Connector(SysInfo.LOCALHOST, SysInfo.DEFAULT_PORT);
                connector.sendCommand(Command.newLoginU(nick, passw));
                up = (UserPackage) connector.getReceivedObject();
                if (!up.hasClientPackage()) {
                    JOptionPane.showMessageDialog(this, "Usuario y/o contraseña incorrectos");
                    txtNick.selectAll();
                    txtNick.requestFocus();
                    connector.closeConnection();
                }
                else{
                    connector.reinstanceStreams();
                    txtNick.setText(null);
                    txtPassword.setText(null);
                    cliPackage = up.getCliPackage();
                    try {
                        System.out.println(cliPackage.getCurrentDir().getCanonicalPath());
                        System.out.println(Arrays.toString(cliPackage.getCurrentDir().listFiles()));
                    } catch (IOException ex) {
                        Logger.getLogger(ClientPackage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(this, "¡Bienvenido "
                            +cliPackage.getUserNick()+"!");
                    setVisible(false);
                    updateAll();
                    openWindow(formClientManagement, this, formClientManagement.getPreferredSize());
                }
                
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "No se ha podido establecer la conexión\n"
                        + "con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Mensaje: "+ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtNickKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNickKeyReleased
        if (evt.getKeyCode() == ENTER_KEY) {
            txtPassword.selectAll();
            txtPassword.requestFocus();
        }
        
    }//GEN-LAST:event_txtNickKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        if (evt.getKeyCode() == ENTER_KEY)
            btnLogin.doClick();
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void listDirectoriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listDirectoriesMouseReleased
        if (evt.getClickCount() == 2) {
            File dir = getSelectedDir();
            
            if (dir != null) {
                try {
                    Command access = new Command("@access", dir.getCanonicalPath());
                    connector.sendObject(access);
                    cliPackage.setCurrentDir((File) connector.getReceivedObject());
                    updateAll();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }//GEN-LAST:event_listDirectoriesMouseReleased

    private void formClientManagementWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formClientManagementWindowClosing
        closeSession();
    }//GEN-LAST:event_formClientManagementWindowClosing

    private void dialogNewFolderWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogNewFolderWindowOpened
        setLocationRelativeTo(formClientManagement);
    }//GEN-LAST:event_dialogNewFolderWindowOpened

    private void txtNewFolderNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewFolderNameKeyReleased
        if (evt.getKeyCode() == ENTER_KEY) {
            String fldName = txtNewFolderName.getText().trim();
            if (!fldName.isEmpty()) try {
                    connector.sendCommand(new Command("@mkd", fldName, cliPackage.getCurrentDir().getCanonicalPath()));
                    connector.sendCommand(new Command("@access", cliPackage.getCurrentDir().getCanonicalPath()));
                    
                    Object obj = connector.getReceivedObject();
                    cliPackage.setCurrentDir((File) obj);
                    updateAll();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            txtNewFolderName.setText(null);
            dialogNewFolder.setVisible(false);
        }
    }//GEN-LAST:event_txtNewFolderNameKeyReleased

    private void itemInfoAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemInfoAccountActionPerformed
        lblUsedSpace.setText(nf.format(cliPackage.getUsedSpaceInMB())+" MB");
        lblFreeSpace.setText(nf.format(cliPackage.getFreeSpaceInMB())+" MB");
        lblTotalSpace.setText(nf.format(cliPackage.getTotalSpaceInMB()/1024)+" GB");
        lblCreationDate.setText(Utilities.getDateToString(
                cliPackage.getAccount().getCreationDate()));
        openWindow(dialogUserInfo, formClientManagement, dialogUserInfo.getPreferredSize());
    }//GEN-LAST:event_itemInfoAccountActionPerformed

    private void btnFolderOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderOptionActionPerformed
//
//        dialogUploadOptions.hide();
//        fileChoos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//        fileChoos.showOpenDialog(formManagement);
//        File[] selectedFiles = fileChoos.getSelectedFiles();
//
//        if (selectedFiles != null) {
//
//            tUploader = new Thread(() -> {
//                try {
//                    for (File file : selectedFiles){
//                        if(file.isDirectory())
//                        linker.uploadFolder(file, linker.getWorkingDirectory());
//
//                        else
//                        linker.uploadFile(file);
//                    }
//                    updateTable();
//                } catch (IOException ex) {
//                    Logger.getLogger(GUIClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            tUploader.start();
//            threadsUploaders.add(tUploader);
//        }
    }//GEN-LAST:event_btnFolderOptionActionPerformed

    private void btnFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileOptionActionPerformed

//        dialogUploadOptions.hide();
//        fileChoos.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int selection = fileChoos.showOpenDialog(formManagement);
//        if (selection == JFileChooser.APPROVE_OPTION) {
//            File[] selectedFiles = fileChoos.getSelectedFiles();
//            if (selectedFiles != null) {
//                Thread t = new Thread(() -> {
//                    try {
//                        for (File file : selectedFiles)
//                        if(file.isFile())
//                        linker.uploadFile(file);
//                        updateTable();
//                    } catch (IOException ex) {
//                        Logger.getLogger(GUIClient.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//                t.start();
//                threadsUploaders.add(t);
//            }
//        }

    }//GEN-LAST:event_btnFileOptionActionPerformed

    private void btnZipOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZipOptionActionPerformed
//
//        dialogUploadOptions.hide();
//        FileFilter filter = FilterSelector.getFilter(FilterSelector.TypeFilter.ZIP);
//        fileChoos.addChoosableFileFilter(filter);
//        fileChoos.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        fileChoos.setFileFilter(filter);
//        int selection = fileChoos.showOpenDialog(formManagement);
//        File[] selectedFiles = fileChoos.getSelectedFiles();
//
//        if (selectedFiles != null) {
//            Thread tUploader = new Thread(() -> {
//
//                for (File file : selectedFiles) {
//                    if (file.getName().endsWith(".zip")) {
//                        try {
//                            linker.uploadZipFile(file);
//                        } catch (IOException ex) {
//                            Logger.getLogger(GUIClient.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            });
//            tUploader.start();
//            threadsUploaders.add(tUploader);
//        }

    }//GEN-LAST:event_btnZipOptionActionPerformed

    private void dialogUploadOptionsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogUploadOptionsWindowOpened
        // openWindow(dialogUploadOptions, formManagement);
        dialogUploadOptions.setLocationRelativeTo(formClientManagement);
    }//GEN-LAST:event_dialogUploadOptionsWindowOpened

    private void btnDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFileActionPerformed
        try {
            deleteFile(getSelectedFileOnTable().getCanonicalPath());
            connector.sendUpdateRequest(cliPackage.getCurrentDirPath(), 
                    cliPackage.getUserNick());

            cliPackage.update(connector.getUpdatesReceived());
            updateTable();
            updateBar();
            JOptionPane.showMessageDialog(dialogFileOptions, "Archivo eliminado correctamente");
            dialogFileOptions.setVisible(false);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteFileActionPerformed

    private void btnRenameFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameFileActionPerformed
        txtNewFolderName.setText(getSelectedFileOnTable().getName());
        txtNewNameFile.requestFocus();
        openWindow(dialogRename, dialogFileOptions);
    }//GEN-LAST:event_btnRenameFileActionPerformed

    private void btnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadFileActionPerformed
        try {
            downloadFile(getSelectedFileOnTable());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDownloadFileActionPerformed

    private void btnGoBackFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackFileActionPerformed
        dialogFileOptions.setVisible(false);
    }//GEN-LAST:event_btnGoBackFileActionPerformed

    private void dialogFileOptionsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogFileOptionsWindowOpened
        //Utilities.resize(dialogFileOptions, formClientManagement);
    }//GEN-LAST:event_dialogFileOptionsWindowOpened

    private void tblFilesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblFilesKeyPressed
        System.out.println("Tecla presionada: "+evt.getKeyCode()+";"+evt.getKeyChar());
        // 127 --> tecla Supr
        
        if (evt.getKeyCode() == ENTER_KEY && tblFiles.getSelectedRowCount() == 1) {
            dialogFileOptions.setVisible(true);
        }
        else if (evt.getKeyCode() == 127) {
            if (tblFiles.getSelectedRowCount() > 1 && evt.getKeyCode() == 127) {
                final int option = JOptionPane.showConfirmDialog(formClientManagement, 
                        "¿Desea eliminar los archivos seleccionados?", 
                        "Confirmación", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                
                if (option == optionSi) {
                    try {
                        deleteSelectedFiles();
                        connector.sendUpdateRequest(cliPackage.getCurrentDir().getCanonicalPath(), 
                                cliPackage.getUserNick());
                        
                        cliPackage.update((UpdatePackage) connector.getReceivedObject());
                        updateBar();
                        updateTable();
                        
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_tblFilesKeyPressed

    private void txtNewNameFileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewNameFileKeyReleased
        renameFile();
    }//GEN-LAST:event_txtNewNameFileKeyReleased

    private void tblFilesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFilesMouseReleased
        if (evt.getClickCount() == 2 && tblFiles.getSelectedColumn() != 0)
            dialogFileOptions.setVisible(true);
    }//GEN-LAST:event_tblFilesMouseReleased

    private void btnDeleteSelectedFIlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedFIlesActionPerformed
        if (tblFiles.getSelectedRowCount() > 1) {
            final int option = JOptionPane.showConfirmDialog(formClientManagement,
                    "¿Desea eliminar los archivos seleccionados?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == optionSi) {
                try {
                    deleteSelectedFiles();
                    connector.sendUpdateRequest(cliPackage.getCurrentDir().getCanonicalPath(),
                            cliPackage.getUserNick());

                    cliPackage.update((UpdatePackage) connector.getReceivedObject());
                    updateBar();
                    updateTable();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(formClientManagement, "No se ha seleccionado ningún archivo");
        }
    }//GEN-LAST:event_btnDeleteSelectedFIlesActionPerformed

    private void btnDeleteFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFile1ActionPerformed
        try {
            connector.sendCommand("@delD", getSelectedDir().getCanonicalPath());
            connector.sendUpdateRequest(cliPackage.getCurrentDirPath(), cliPackage.getUserNick());
        } catch (IOException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteFile1ActionPerformed

    private void btnRenameFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameFile1ActionPerformed
    }//GEN-LAST:event_btnRenameFile1ActionPerformed

    private void btnDownloadFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadFile1ActionPerformed
        try {
            configFileChooser(For.DIRECTORIES);
            fileChoos.showOpenDialog(formClientManagement);
            final File localPath = fileChoos.getSelectedFile();

            connector.sendCommand("@dwnD", localPath.getCanonicalPath(), 
                    getSelectedDir().getCanonicalPath());

            Folder downloaded = (Folder) connector.getReceivedObject();
            downloaded.create();
            JOptionPane.showMessageDialog(dialogFolderOptions, "Directorio descargado completamente\n"
                    + "Guardado en la carpeta "+localPath.getName());
            dialogFolderOptions.setVisible(false);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDownloadFile1ActionPerformed

    private void btnGoBackFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackFile1ActionPerformed
        dialogFolderOptions.setVisible(false);
    }//GEN-LAST:event_btnGoBackFile1ActionPerformed

    private void dialogFolderOptionsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogFolderOptionsWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_dialogFolderOptionsWindowOpened

    private void btnFolderOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderOptionsActionPerformed
        openWindow(dialogFolderOptions, formClientManagement);
    }//GEN-LAST:event_btnFolderOptionsActionPerformed

    public void updateList(File directory){
        listDirectories.setModel(new LMDirectories(directory));
        listDirectories.setCellRenderer(new RenderListDirs());
        listDirectories.updateUI();
    }
    
    public void updateList(){
        updateList(cliPackage.getCurrentDir());
    }
    
    public void updateTable(){
        //Grapher.getGrapher().graphFiles(cliPackage.getOnlyFiles(), panelFiles);
        tblFiles.setModel(new TMFiles(cliPackage.getOnlyFiles()));
        tblFiles.setDefaultRenderer(Object.class, new TCRFiles());
        tblFiles.setRowHeight(25);
    }
    
    public void updateBar(){
            final double currentUsedSpace = cliPackage.getUsedSpaceInMB();
            final String totalSpace = nf.format(cliPackage.getTotalSpaceInMB()/1024);
            spaceBar.setModel(new PBModel(cliPackage.getAccount()));

            if (currentUsedSpace / 1024 < 1)
                spaceBar.setString(nf.format(currentUsedSpace) + " MB/" + totalSpace + "GB");
            else
                spaceBar.setString(nf.format(currentUsedSpace/1024) + " GB/" + totalSpace + "GB");
    }

    public void updateCurDir(){
        lblCurrentDir.setText(lblCurrentDir.getText().substring(0, 19));
        lblCurrentDir.setText(lblCurrentDir.getText()+cliPackage.getFormattedCurrentDir());
    }
    
    public void updateUserName(){
        lblUserName.setText(cliPackage.getUserNick());
    }
    
    public void updateAll(){
        try {
            updateList();
            updateTable();
            updateBar();
            cliPackage.createFormattedCurrentDir();
            updateCurDir();
        } catch (IOException ex) {
            Logger.getLogger(GUICloudClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUICloudClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUICloudClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUICloudClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUICloudClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        GUICloudClient.newInstance();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFolder;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDeleteFile;
    private javax.swing.JButton btnDeleteFile1;
    private javax.swing.JButton btnDeleteSelectedFIles;
    private javax.swing.JButton btnDownloadFile;
    private javax.swing.JButton btnDownloadFile1;
    private javax.swing.JButton btnFileOption;
    private javax.swing.JButton btnFolderOption;
    private javax.swing.JButton btnFolderOptions;
    private javax.swing.JButton btnGoBackFile;
    private javax.swing.JButton btnGoBackFile1;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnOrderFiles;
    private javax.swing.JButton btnRegUser;
    private javax.swing.JButton btnRenameFile;
    private javax.swing.JButton btnRenameFile1;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateDirectory;
    private javax.swing.JButton btnUploadFile;
    private javax.swing.JButton btnZipOption;
    private javax.swing.JComboBox<String> cboOrderOption;
    private javax.swing.JComboBox<String> cboOrderType;
    private javax.swing.JDialog dialogFileOptions;
    private javax.swing.JDialog dialogFolderOptions;
    private javax.swing.JDialog dialogNewFolder;
    private javax.swing.JDialog dialogRename;
    private javax.swing.JDialog dialogUploadOptions;
    private javax.swing.JDialog dialogUserInfo;
    private javax.swing.JFrame formClientManagement;
    private javax.swing.JFrame formClientRegister;
    private javax.swing.JMenuItem itemInfoAccount;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCreationDate;
    private javax.swing.JLabel lblCurrentDir;
    private javax.swing.JLabel lblFreeSpace;
    private javax.swing.JLabel lblTotalSpace;
    private javax.swing.JLabel lblUsedSpace;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JList<String> listDirectories;
    private javax.swing.JScrollPane listFIles;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelDirectories;
    private javax.swing.JPanel panelFiles;
    private javax.swing.JPanel panelIcons;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelUploadOptions;
    private javax.swing.JProgressBar spaceBar;
    private javax.swing.JTable tblFiles;
    private javax.swing.JTextField txtNewFolderName;
    private javax.swing.JTextField txtNewNameFile;
    private javax.swing.JTextField txtNick;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtRegNick;
    private javax.swing.JPasswordField txtRegPass1;
    private javax.swing.JPasswordField txtRegPass2;
    // End of variables declaration//GEN-END:variables
}
