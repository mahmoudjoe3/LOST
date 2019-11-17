package com.joe_mamdoh_shafik.lost;

public class Items {

    private String Name;
    private String Phone;
    private String LostName;
    private String Location;
    private String ImageUrl;
    private String Description;
    private String CategoryName;

    public Items() {
    }

    public Items(String categoryName,String name, String phone, String lostName, String location, String imageUrl, String description) {
        Name = name;
        Phone = phone;
        LostName = lostName;
        Location = location;
        ImageUrl = imageUrl;
        Description = description;
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLostName() {
        return LostName;
    }

    public void setLostName(String lostName) {
        LostName = lostName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
