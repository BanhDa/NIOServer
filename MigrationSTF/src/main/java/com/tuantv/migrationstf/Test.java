/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf;

import com.tuantv.migrationstf.domain.FileInfo;

/**
 *
 * @author tuantran
 */
public class Test {

    public static void main(String[] args) {
        testContructor();
    }

    private static void testContructor() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId("1");

        FileInfo file = new FileInfo(fileInfo);
        System.out.println("file : " + file);
    }
}
