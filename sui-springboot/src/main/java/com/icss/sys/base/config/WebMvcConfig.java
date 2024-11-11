package com.icss.sys.base.config;

import com.icss.sys.base.properteis.FileUploadProperteis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private FileUploadProperteis fileUploadProperteis;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileUploadProperteis.getStaticAccessPath()).addResourceLocations("file:" + fileUploadProperteis.getDiskPath() + fileUploadProperteis.getFileBasePath());
    }

}