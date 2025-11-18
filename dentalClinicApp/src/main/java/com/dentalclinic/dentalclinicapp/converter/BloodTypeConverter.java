package com.dentalclinic.dentalclinicapp.converter;

import com.dentalclinic.dentalclinicapp.entity.Patient.BloodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for BloodType enum
 * Converts between database string values (e.g., "O+") and enum constants
 * (e.g., O_POSITIVE)
 */
@Converter(autoApply = true)
public class BloodTypeConverter implements AttributeConverter<BloodType, String> {

    @Override
    public String convertToDatabaseColumn(BloodType bloodType) {
        if (bloodType == null) {
            return null;
        }
        return bloodType.getValue();
    }

    @Override
    public BloodType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        for (BloodType bloodType : BloodType.values()) {
            if (bloodType.getValue().equals(value)) {
                return bloodType;
            }
        }

        throw new IllegalArgumentException("Unknown blood type value: " + value);
    }
}
