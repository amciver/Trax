/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.experiment.trax.services;

import android.util.Log;

import java.util.Properties;

public class PropertyLoader {

    private boolean hasCredentials = false;
    private String tokenVendingMachineURL = null;
    private boolean useSSL = false;

    private static PropertyLoader instance = null;

    public static PropertyLoader getInstance() {
        if (instance == null) {
            instance = new PropertyLoader();
        }

        return instance;
    }

    public PropertyLoader() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("AwsCredentials.properties"));

            this.tokenVendingMachineURL = properties.getProperty("tokenVendingMachineURL");
            this.useSSL = Boolean.parseBoolean(properties.getProperty("useSSL"));

            if (this.tokenVendingMachineURL == null || this.tokenVendingMachineURL.equals("") || this.tokenVendingMachineURL.equals("CHANGEME")) {
                this.tokenVendingMachineURL = null;
                this.useSSL = false;
                this.hasCredentials = false;
            } else {
                this.hasCredentials = true;
            }
        } catch (Exception exception) {
            Log.e("PropertyLoader", "Unable to read property file.");
        }
    }

    public boolean hasCredentials() {
        return this.hasCredentials;
    }

    public String getTokenVendingMachineURL() {
        return this.tokenVendingMachineURL;
    }

    public boolean useSSL() {
        return this.useSSL;
    }

}
