package com.haduart.dto;

import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: haduart
 * Date: 03/12/14
 * Time: 07:00
 * To change this template use File | Settings | File Templates.
 */
public interface ViewDTO {

    public long getUserId();

    public void setUserId(long userId);

    public String getTimeStamp();

    public void setTimeStamp(String ts);
}
