/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
/**
 *
 * @author martin
 */
public class Command implements Serializable{
    
    private String order;
    private String[] options;
    
    private static final StringBuilder sBuilder = new StringBuilder();
    
    // @uplF rutaLocal rutaRemota --> subir archivo
    /**
     * @uplF rutaLocal rutaRemota --> subir archivo
     */
    public static final Command uplF = Command.newInstance("@uplF");

    // @dwnF rutaLocal rutaRemota --> bajar archivo
    /**
     * @dwnF rutaLocal rutaRemota --> bajar archivo
     */
    public static final Command dwnF = Command.newInstance("@dwnF");

    // @cpF rutaActual rutaNueva--> copiar archivo
    /**
     * @cpF rutaActual rutaNueva--> copiar archivo
     */
    public static final Command cpF = Command.newInstance("@cpF");

    // @cpF rutaActual rutaNueva--> copiar archivo
    /**
     * @cpF rutaActual rutaNueva--> copiar archivo
     */
    public static final Command ctF = Command.newInstance("@ctF");

    // @delF rutaActual --> borrar archivo
    /**
     * @delF rutaActual --> borrar archivo
     */
    public static final Command delF = Command.newInstance("@delF");

    // @root --> volver a raiz
    /**
     * @root --> volver a raiz
     */
    public static final Command root = Command.newInstance("@root");

    // @list rutaCarpetaRemota --> listar archivos de una carpeta específica
    /**
     * @list rutaCarpetaRemota --> listar archivos de una carpeta específica
     */
    public static final Command list = Command.newInstance("@list");

    // @uplD rutaLocal rutaRemota --> subir carpeta
    /**
     * @uplD rutaLocal rutaRemota --> subir carpeta
     */
    public static final Command uplD = Command.newInstance("@dwnF");

    // @dwnD rutaLocal rutaRemota --> bajar carpeta
    /**
     * @dwnD rutaLocal rutaRemota --> bajar carpeta
     */
    public static final Command dwnD = Command.newInstance("@dwnD");

    // @cpD rutaActual rutaNueva --> copiar carpeta
    /**
     * @cpD rutaActual rutaNueva --> copiar carpeta
     */
    public static final Command cpD = Command.newInstance("@cpD");

    // @ctD rutaActual rutaNueva --> cortar carpeta
    /**
     * @ctD rutaActual rutaNueva --> cortar carpeta
     */
    public static final Command ctD = Command.newInstance("@ctD");

    // @delD rutaRemota --> borrar carpeta
    /**
     * @delD rutaRemota --> borrar carpeta
     */
    public static final Command delD = Command.newInstance("@delD");

    // @back rutaActual --> volver atras
    /**
     * @back rutaActual --> volver atras
     */
    public static final Command back = Command.newInstance("@back");

    // @access rutaCompleta --> acceder a una carpeta del directorio actual
    /**
     * @access rutaCompleta --> acceder a una carpeta del directorio actual
     */
    public static final Command access = Command.newInstance("@access");

    // @regU nick password --> registrar usuario
    /**
     * @regU nick password --> registrar usuario
     */
    public static final Command regU = Command.newInstance("@regU");

    // @loginU nick password --> logearse
    /**
     * @loginU nick password --> logearse
     */
    public static final Command loginU = Command.newInstance("@loginU");
    
    
    // @close --> cierra la conexion
        /**
     * @close --> cierra la conexion
     */
    
    public static final Command close = Command.newInstance("@close");
    
    // @mkd folderName remotePath --> crea una carpeta
    /**
     * @mkd folderName remotePath --> crea una carpeta
     */
    public static final Command mkd = Command.newInstance("@mkd");
    
    // @update rutaCompletaCurDir nickUser
    /**
     * @update rutaCompletaCurDir nickUser
     */
    public static final Command update = Command.newInstance("@update");

    // @rename rutaCompleta nuevoNombre
    /**
     * @rename rutaCompleta nuevoNombre
     */
    
    public static final Command rename = Command.newInstance("@rename");
    
    // @start -p puerto || @start -p puerto -t tiempoEnSegundos
    /**
     * @start -p puerto || @start -p puerto -t tiempoEnSegundos
     */
    public static final Command start = Command.newInstance("@start");
    
    public static Command newInstance(String strCommand){
        return new Command(strCommand);
    }
    
    //private static final long serialVersionUID = SysInfo.SVU_COMMAND;
    
    public Command(String strCommand) {
        //LinkedBlockingQueue<String> queue;
//        final String[] split = strCommand.split(" ");
//        order = split[0];
//        options = new String[split.length-1];
//        for (int i = 1; i < split.length; i++)
//            options[i-1] = split[i];
        this(strCommand.split(" "));
    }

    public Command(String... strCommand){
        final int len = strCommand.length;
        //System.out.println("Cantidad de opciones del comando: " + (len-1));
        order = strCommand[0];
        if (len == 1) options = null;
        
        else{
            options = new String[len-1];
            for (int i = 1; i < len; i++)
                options[i-1] = strCommand[i];
        }
    }
    
    private static void clearStringBuilder(){
        sBuilder.delete(0, sBuilder.length());
    }
    
    private static void appendToBuilder(String str){
        sBuilder.append(str);
    }
    
    private static void appendToBuilder(String... strs){
        for (String str : strs)
            sBuilder.append(str);
    }

    public boolean hasOptions(){
        return options != null && options.length > 0;
    }

    public void addOption(String option){
        if(option == null) return;
        
        if (!hasOptions()) {
            options = new String[1];
            options[0] = option;
        }
        
        else{
            final String[] aux = new String[options.length+1];
            System.arraycopy(options, 0, aux, 0, options.length);
            aux[aux.length-1] = option;
            options = aux;
        }
        
    }
    
//    public boolean isValid(){
//        if(order == null) return false;
//        if(!order.startsWith("@") || options == null) return false;
//        return this.isEquals(Command.access) || this.isEquals(Command.back) ||
//                this.isEquals(Command.cpD) || this.isEquals(Command.cpF) ||
//                this.isEquals(Command.ctD) || this.isEquals(Command.ctF) ||
//                this.isEquals(Command.delD) || this.isEquals(Command.delF) ||
//                this.isEquals(Command.dwnD) || this.isEquals(Command.dwnF) ||
//                this.isEquals(Command.list) || this.isEquals(Command.loginU) ||
//                this.isEquals(Command.regU) || this.isEquals(Command.root) ||
//                this.isEquals(Command.uplD) || this.isEquals(Command.uplF);
//    }

    public int getOptionsCount(){
        return hasOptions() ? options.length : 0;
    }
    
    public boolean isValid(){
        if(order == null) return false;
        if(!order.startsWith("@")) return false;

        boolean isValid = 
                order.equals("@access") || order.equals("@back") || order.equals("@cpD") ||
                order.equals("@cpF") || order.equals("@ctD") || order.equals("@ctF") ||
                order.equals("@delD") || order.equals("@delF") || order.equals("@dwnD") ||
                order.equals("@dwnF") || order.equals("@list") || order.equals("@loginU") ||
                order.equals("@regU") || order.equals("@root") || order.equals("@uplD") ||
                order.equals("@uplF") || order.equals("@close") || order.equals("@mkd") ||
                order.equals("@update") || order.equals("@rename") || order.equals("@start");

        if(!isValid || !order.equals("@start")) return isValid;
        
        // Ahora se supone que el comando es start
        if(!hasOptions()) return false;
        if(getOptionsCount() != 2 && getOptionsCount() != 4) return false;
        
        // Ahora suponiendo que tengo la cantidad de opciones adecuada
        // pero se debe comprobar si estan bien escritas y las opciones
        // escritas son las correctas
        
        if(!getOptionAt(0).equals("-p")) return false;
        
        try {
            if (!getOptionAt(1).equals("default"))
                Integer.parseInt(getOptionAt(1));
            
            if(getOptionsCount() == 4){
                Integer.parseInt(getOptionAt(3));
                return getOptionAt(2).equals("-t");
            }
            else
                return true;
            
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean isEqualsOrder(Command otherCommand){
        return this.order.equalsIgnoreCase(otherCommand.getOrder());
    }
    
    public boolean isEquals(Command otherCommand){
        boolean isEquals = otherCommand.isValid() && isValid();
        if(!isEquals) return isEquals;
    
        isEquals = order.equalsIgnoreCase(otherCommand.getOrder());
        if(!isEquals) return isEquals;
        
        return Arrays.equals(options, otherCommand.getOptions());
    }

    public String getOrder() {
        return order;
    }

    public String getFirstOption(){
        //System.out.println((options != null) ? options.length : "Es nulo");
        return (hasOptions()) ? options[0] : null;
    }
    
    public String[] getOptions() {
        return options;
    }

    public void setOptions(String... newOptions){
        if(newOptions == null) return;
        options = newOptions;
    }
    
    /**
     * Obtiene las opciones de este objeto como una lista
     * @return Objeto de tipo LinkedList con las opciones de este comando
     */
    public LinkedList<String> getOptionsAsList(){
        return new LinkedList<>(Arrays.asList(options));
    }
    
    /**
     * Devuelve un array con las opciones del comando dentro
     * de los índices establecidos. Si los índices establecidos
     * están fuera de los límites del arreglo o si estos son negativos o
     * si start es mayor o igual que end, este método devolverá null
     * @param start Índice inicial del arreglo options en donde
     * comienza el rescate de los datos
     * @param end Índice limitador del arreglo options en donde
     * terminará la copia(no se considerará el elemento de este 
     * indice para el nuevo arreglo)
     * @return Arreglo que contiene los elementos contenidos dentro de los 
     * límites establecidos.
     */
    public String[] getSubOptions(int start, int end){
        if (options != null) {
            if ((start > 0 && end > 0) && (start < end)) {
                String[] newArray = new String[end-start];
                for (int i = 0; i < newArray.length; i++)
                    newArray[i] = options[start+i];
                return newArray;
            }
        }
        return null;
    }
    
    /**
     * Entrega una opción del arreglo options dada por su índice, si el arreglo
     * no cumple con las condiciones solicitadas (es nulo, el índice es negativo o 
     * está fuera de los límites por el número de elementos del arreglo) el resultado
     * devuelto por este método será null.
     * @param index Índice de la opción solicitada
     * @return La opción solicitada por el índice
     */
    public String getOptionAt(int index){
        if(!hasOptions()) return null;
        return (options.length > index && index >= 0) ? options[index] : null;
    }

    public synchronized static Command newRegU(String... options){
         String strCommand = "@regU ";

        appendToBuilder(strCommand);
        for (String option : options){
            appendToBuilder(option, " ");
            option = null;
        }
        
        String cmd = sBuilder.toString();
        clearStringBuilder();
        strCommand = null;
        return new Command(cmd);
    }
    
    public synchronized static Command newLoginU(String... options){
        String strCommand = "@loginU ";

        appendToBuilder(strCommand);
        for (String option : options){
            appendToBuilder(option, " ");
            option = null;
        }
        
        String cmd = sBuilder.toString();
        clearStringBuilder();
        strCommand = null;
        return new Command(cmd);
    }

    @Override
    public String toString() {
        return "Command{" + "order=" + order + ", options=" + Arrays.toString(options) + '}';
    }

}
