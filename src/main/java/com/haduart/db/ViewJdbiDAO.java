package com.haduart.db;


import com.haduart.dto.ViewDTO;
import com.haduart.mappers.ViewMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;


public interface ViewJdbiDAO {

    @SqlUpdate("INSERT INTO views SET profileId = :profileId, userId = :userId, ts = :ts")
    void insert(@Bind("profileId") long profileId, @Bind("userId") long userId, @Bind("ts") Date ts);

    @SqlQuery("SELECT userId, ts FROM views WHERE profileId = :profileId AND ts BETWEEN :olderDate and :newerDate ORDER BY ts desc limit 10")
    @Mapper(ViewMapper.class)
    List<ViewDTO> findViewsByProfile(@Bind("profileId") long profileId,
                                     @Bind("newerDate") DateTime newerDate,
                                     @Bind("olderDate") DateTime olderDate);


    /**
     * close with no args is used to close the connection
     */
    void close();
}
