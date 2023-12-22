package com.home.traininfo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    @DisplayName("Train with ON_STATION status")
    void testGetStatus(){
        assertEquals(Status.ON_STATION, Status.getStatus("ON_STATION"));
    }

    @Test
    @DisplayName("Train with INCOMING status")
    void testGetStatus2(){
        assertEquals(Status.INCOMING, Status.getStatus("INCOMING"));
    }

    @Test
    @DisplayName("Train with DEPARTED status")
    void testGetStatus3(){
        assertEquals(Status.DEPARTED, Status.getStatus("DEPARTED"));
    }

    @Test
    @DisplayName("Train with UNKNOWN status")
    void testGetStatus4(){
        assertEquals(Status.INCOMING, Status.getStatus("UNKNOWN"));
    }

}