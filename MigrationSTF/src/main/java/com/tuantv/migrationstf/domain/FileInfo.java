/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author tuantran
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileInfo extends Domain{
    
    private String userId;
    
    private String url;
    
    private Long uploadTime;
}
