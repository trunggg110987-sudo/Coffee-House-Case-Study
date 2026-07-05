package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.DiningTable;
import com.coffeeshopmanagement.entity.TableStatus;
import com.coffeeshopmanagement.repository.DiningTableRepository;
import com.coffeeshopmanagement.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiningTableServiceImpl implements DiningTableService {

    private final DiningTableRepository tableRepository;
    private final DiningTableRepository diningTableRepository;

    @Override
    public List<DiningTable> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public DiningTable getTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn có ID: " + id));
    }

    @Override
    @Transactional
    public void saveTable(DiningTable table) {
        tableRepository.save(table);
    }

    @Override
    @Transactional
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleTableStatus(Long id) {
        DiningTable table = getTableById(id);
        if (table.getStatus() == TableStatus.AVAILABLE) {
            table.setStatus(TableStatus.OCCUPIED);
        } else {
            table.setStatus(TableStatus.AVAILABLE);
        }
        tableRepository.save(table);
    }

    @Override
    @Transactional
    public void updateTableStatus(Long id, String status) {
        DiningTable table = diningTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bàn không tồn tại"));

        table.setStatus(TableStatus.valueOf(status));
        diningTableRepository.save(table);
    }
}
