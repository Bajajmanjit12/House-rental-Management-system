public class Property {
    private int propertyId;
    private String address;
    private String city;
    private String type;
    private double monthlyRent;
    private String availabilityStatus;

    public Property(int propertyId, String address, String city, String type,
                    double monthlyRent, String availabilityStatus) {
        this.propertyId = propertyId;
        this.address = address;
        this.city = city;
        this.type = type;
        this.monthlyRent = monthlyRent;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters
    public int getPropertyId() { return propertyId; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getType() { return type; }
    public double getMonthlyRent() { return monthlyRent; }
    public String getAvailabilityStatus() { return availabilityStatus; }
}