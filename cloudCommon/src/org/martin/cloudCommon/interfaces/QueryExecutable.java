/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author martin
 */
public interface QueryExecutable {
    public void exec(String query) throws SQLException;
    public ResultSet execSelect(String query) throws SQLException;
    public void insert(String table, Object...fields) throws SQLException;
    //public ResultSet select(String table, String where, Object... fields) throws SQLException;
    public ResultSet select(String functionName, Object... parameters) throws SQLException;
    public ResultSet select(String functionName) throws SQLException;
    public void update(String table, String where, String... sets) throws SQLException;
    public void delete(String table, String where) throws SQLException;
    public ResultSet call(String procedureName, Object... atributes) throws SQLException;
    public ResultSet call(String procedureName) throws SQLException;
}
