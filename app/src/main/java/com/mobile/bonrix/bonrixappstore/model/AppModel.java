package com.mobile.bonrix.bonrixappstore.model;

public class AppModel {

    private String Id;
    private String ApkName;
    private String publishdate;
    private String ImageUrl;
    private String Name;
    private String Package;
    private String Version;
    private String ApkPath;

    public String getApkPath() {
        return ApkPath;
    }

    public void setApkPath(String apkPath) {
        ApkPath = apkPath;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public String getApkName() {
        return ApkName;
    }

    public void setApkName(String apkName) {
        ApkName = apkName;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
