/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author martin
 */
public class Command implements Serializable{
    private final String order;
    private final String[] options;
    
    // @uplF rutaLocal rutaRemota --> subir archivo
    public static final Command uplF = Command.newInstance("uplF");

    // @dwnF rutaLocal rutaRemota --> bajar archivo
    public static final Command dwnF = Command.newInstance("@dwnF");

    // @cpF rutaActual rutaNueva--> copiar archivo
    public static final Command cpF = Command.newInstance("@cpF");

    // @ctF rutaActual rutaNueva--> cortar archivo
    public static final Command ctF = Command.newInstance("@ctF");

    // @delF rutaActual --> borrar archivo
    public static final Command delF = Command.newInstance("@delF");

    // @root --> volver a raiz
    public static final Command root = Command.newInstance("@root");

    // @list rutaCarpetaRemota --> listar archivos de una carpeta específica
    public static final Command list = Command.newInstance("@list");

    // @uplD rutaLocal rutaRemota --> subir carpeta
    public static final Command uplD = Command.newInstance("@dwnF");

    // @dwnD rutaLocal rutaRemota --> bajar carpeta
    public static final Command dwnD = Command.newInstance("@dwnD");

    // @cpD rutaActual rutaNueva --> copiar carpeta
    public static final Command cpD = Command.newInstance("@cpD");

    // @ctD rutaActual rutaNueva --> cortar carpeta
    public static final Command ctD = Command.newInstance("@ctD");

    // @delD rutaRemota --> borrar carpeta
    public static final Command delD = Command.newInstance("@delD");

    // @back rutaActual --> volver atras
    public static final Command back = Command.newInstance("@back");

    // @access rutaCompleta --> acceder a una carpeta del directorio actual
    public static final Command access = Command.newInstance("@access");

    // @regU nick password --> registrar usuario
    public static final Command regU = Command.newInstance("@regU");

    // @loginU nick password --> logearse
    public static final Command loginU = Command.newInstance("@loginU");
    
    public static Command newInstance(String strCommand){
        return new Command(strCommand);
    }
    
    public Command(String strCommand) {
        final String[] split = strCommand.split(" ");
        order = split[0];
        options = new String[split.length-1];
        for (int i = 1; i < split.length; i++)
            options[i-1] = split[i];
    }
    
    public boolean isValid(){
        return (!order.isEmpty()) && order.startsWith("@") && options != null;
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

    public String[] getOptions() {
        return options;
    }
}
