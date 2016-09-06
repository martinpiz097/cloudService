/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.martin.cloudCommon.interfaces.QueryExecutable;

/**
 *
 * @author martin
 */
public class DbConnection implements QueryExecutable{
    
    private final Connection con;
    private Statement sen;
    private final MysqlDataSource mysqlDs;
    private String strQuery;
    //private StringBuffer strBuff;
    
    public DbConnection(String server, String dbName, String user, String password) throws SQLException {
        mysqlDs = new MysqlDataSource();
        mysqlDs.setServerName(server);
        mysqlDs.setUser(user);
        mysqlDs.setPassword(password);
        mysqlDs.setDatabaseName(dbName);
        con = mysqlDs.getConnection();
    }

    public int getTableCount(String table) throws SQLException{
        final int count;
        ResultSet res = execSelect("select count(id) from " + table);
        if (res.next()) count = res.getInt(1);
        else count = 0;
        return count;
    }
    
    public int getTableCount(String table, String where) throws SQLException{
        final int count;
        try (ResultSet res = execSelect("select count(id) from " + table + " where " + where)) {
            if (res.next()) count = res.getInt(1);
            else count = 0;
        }
        return count;
    }

    @Override
    public void exec(String query) throws SQLException {
        con.createStatement().execute(query);
    }
    
    @Override
    public ResultSet execSelect(String query) throws SQLException{
        return con.createStatement().executeQuery(query);
    }
    
    @Override
    public void insert(String table, Object... fields) throws SQLException {
        strQuery = "insert into " + table + " values (";
        for (int i = 0; i < fields.length; i++)
            if (i < fields.length-1)
                strQuery+=("'"+fields[i]+"',");
            else
                strQuery+=("'"+fields[i]+"')");
        con.createStatement().execute(strQuery);
    }

    @Override
    public ResultSet select(String functionName, Object... parameters) throws SQLException{
        strQuery = "select " + functionName + "(";
        final int lenParameters = parameters.length;
        for (int i = 0; i < lenParameters; i++)
            if (i < lenParameters-1)
                strQuery+=("'"+parameters[i]+"',");
            else
                strQuery+=("'"+parameters[i]+"')");
    
        return con.createStatement().executeQuery(strQuery);
    }

    @Override
    public ResultSet select(String functionName) throws SQLException{
        return con.createStatement().executeQuery("select "+functionName+"()");
    }
    
    @Override
    public void update(String table, String where, String... sets) throws SQLException {
        strQuery = "update " + table + " set ";
        for (int i = 0; i < sets.length; i++)
            if (i < sets.length-1)
                strQuery+=(sets[i] + ", ");
            else
                strQuery+=sets[i];
        
        if (where != null) strQuery+=(" where " + where);
        
        con.createStatement().execute(strQuery);
    }

    @Override
    public void delete(String table, String where) throws SQLException {
        con.createStatement().execute("delete from " + table + " + where " + where);
    }

    @Override
    public ResultSet call(String procedureName, Object... atributes) throws SQLException {
        strQuery = "call " + procedureName + "(";
        for (int i = 0; i < atributes.length; i++)
            if (i<atributes.length-1)
                strQuery+=("'"+atributes[i]+"',");
            else
                strQuery+=("'"+atributes[i]+"'");
        
        strQuery+=")";
        
        return con.createStatement().executeQuery(strQuery);
    }
    
    @Override
    public ResultSet call(String procedureName) throws SQLException{
        strQuery = "call " + procedureName + "()";
        return con.createStatement().executeQuery(strQuery);
    }

}
