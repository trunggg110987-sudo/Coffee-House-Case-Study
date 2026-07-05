package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.DiningTable;
import java.util.List;

public interface DiningTableService {
    List<DiningTable> getAllTables();
    DiningTable getTableById(Long id);
    void saveTable(DiningTable table);
    void deleteTable(Long id);
    void toggleTableStatus(Long id);
    void updateTableStatus(Long id, String status);
}
