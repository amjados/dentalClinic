package com.dentalclinic.dentalclinicapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite ID class for EmployeeClinicAccess
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeClinicAccessId implements Serializable {
    private Long employee;
    private Long clinic;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmployeeClinicAccessId that = (EmployeeClinicAccessId) o;
        return Objects.equals(employee, that.employee) && Objects.equals(clinic, that.clinic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, clinic);
    }
}
