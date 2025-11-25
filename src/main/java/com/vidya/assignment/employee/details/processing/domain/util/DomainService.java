package com.vidya.assignment.employee.details.processing.domain.util;

import java.util.List;

public interface DomainService<T> {

    //T get(Long id);

    T create(T t);

    T update(String employeeId, T t);

    List<T> getAll();

    String delete(String employeeId);

    T getByEmployeeId(String employeeId);
}
