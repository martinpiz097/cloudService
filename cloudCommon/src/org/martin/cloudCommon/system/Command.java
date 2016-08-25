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
    
    private final String order;
    private final String[] options;
    
    // @uplF rutaLocal rutaRemota --> subir archivo
    /**
     * @uplF rutaLocal rutaRemota --> subir archivo
     */
    public static final Command uplF = Command.newInstance("uplF");

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
    
    public static Command newInstance(String strCommand){
        return new Command(strCommand);
    }
    
    //private static final long serialVersionUID = SysInfo.SVU_COMMAND;
    
    public Command(String strCommand) {
        //LinkedBlockingQueue<String> queue;
        final String[] split = strCommand.split(" ");
        order = split[0];
        options = new String[split.length-1];
        for (int i = 1; i < split.length; i++)
            options[i-1] = split[i];
    }

    public Command(String... strCommand){
        final int len = strCommand.length;
        order = strCommand[0];
        if (len == 1) options = null;
        
        else{
            options = new String[len-1];
            for (int i = 1; i < len; i++)
                strCommand[i] = options[i-1];
        }
    }

    public boolean hasOptions(){
        return options != null;
    }
    
    public boolean isValid(){
        if(order == null) return false;
        if(!order.startsWith("@") || options == null) return false;
        return this.isEquals(Command.access) || this.isEquals(Command.back) ||
                this.isEquals(Command.cpD) || this.isEquals(Command.cpF) ||
                this.isEquals(Command.ctD) || this.isEquals(Command.ctF) ||
                this.isEquals(Command.delD) || this.isEquals(Command.delF) ||
                this.isEquals(Command.dwnD) || this.isEquals(Command.dwnF) ||
                this.isEquals(Command.list) || this.isEquals(Command.loginU) ||
                this.isEquals(Command.regU) || this.isEquals(Command.root) ||
                this.isEquals(Command.uplD) || this.isEquals(Command.uplF);
    }
    
    public boolean isEqualsOrder(Command otherCommand){
        return this.order.equalsIgnoreCase(otherCommand.getOrder());
    }
    
    public boolean isEquals(Command otherCommand){
        boolean isEquals = otherCommand.isValid() && isValid();
        if(!isEquals) return isEquals;
        isEquals = order.equalsIgnoreCase(otherCommand.getOrder());
        if(!isEquals) return isEquals;
        isEquals = Arrays.equals(options, otherCommand.getOptions());
        return isEquals;
    }

    public String getOrder() {
        return order;
    }

    public String getFirstOption(){
        return (options != null) ? options[0] : null;
    }
    
    public String[] getOptions() {
        return options;
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
    public String getOption(int index){
        if(options == null) return null;
        return (options.length >= index+1 && index >= 0) ? options[index] : null;
    }

    public static Command newRegU(String... options){
        String strCommand = "@regU ";
        for (String option : options) 
            strCommand += (option + " ");
        return new Command(strCommand);
    }
    
    public static Command newLoginU(String... options){
        String strCommand = "@loginU ";
        for (String option : options) 
            strCommand += (option + " ");
        return new Command(strCommand);
    }
}
