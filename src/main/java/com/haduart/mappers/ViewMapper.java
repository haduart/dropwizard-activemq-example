package com.haduart.mappers;

import com.haduart.core.View;
import com.haduart.dto.ViewDTO;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewMapper implements ResultSetMapper<ViewDTO> {
    @Override
    public ViewDTO map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new View(resultSet.getLong("userId"), resultSet.getDate("ts"));
    }
}
