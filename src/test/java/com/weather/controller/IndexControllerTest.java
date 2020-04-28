package com.weather.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexControllerTest {

	@Test
    public void testHomeController() {
        IndexController index = new IndexController();
        String result = index.index();
        assertEquals(result, "index.html");
    }
	
}
