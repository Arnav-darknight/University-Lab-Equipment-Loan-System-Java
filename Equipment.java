import java.time.LocalDate;

//----------------Represents a piece of equipment in the university lab ------------------//
public class Equipment {
    private int equipmentId;
    private String name;
    private String manufacturer;
    private String model;
    private LocalDate purchaseDate;
    private String category;
    private boolean available;

    public Equipment(int equipmentId, String name, String manufacturer, String model,
                     LocalDate purchaseDate, String category) {
        if (equipmentId < 0) {
            throw new IllegalArgumentException("Equipment ID cannot be negative.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Equipment name cannot be empty.");
        }
        if (manufacturer == null || manufacturer.isBlank()) {
            throw new IllegalArgumentException("Manufacturer cannot be empty.");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if (purchaseDate == null) {
            throw new IllegalArgumentException("Purchase date is required.");
        }
        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        this.equipmentId = equipmentId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.purchaseDate = purchaseDate;
        this.category = category;
        this.available = true;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Equipment name cannot be empty.");
        }
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        if (manufacturer == null || manufacturer.isBlank()) {
            throw new IllegalArgumentException("Manufacturer cannot be empty.");
        }
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        this.model = model;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        if (purchaseDate == null) {
            throw new IllegalArgumentException("Purchase date is required.");
        }
        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        this.purchaseDate = purchaseDate;
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category;
    }

    public void markasBorrowed() {
        if (!available) {
            throw new IllegalStateException("Equipment is already on loan.");
        }
        available = false;
    }

    public void markasReturned() {
        if (available) {
            throw new IllegalStateException("Equipment is already marked as available.");
        }
        available = true;
    }

    @Override
    public String toString() {
        return "ID: " + equipmentId
                + ", Name: " + name
                + ", Manufacturer: " + manufacturer
                + ", Model: " + model
                + ", Purchase Date: " + purchaseDate
                + ", Category: " + category
                + ", Available: " + available;
    }
}