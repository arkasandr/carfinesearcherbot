package ru.arkasandr.carfinesearcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.arkasandr.carfinesearcher.model.GibddRequest;

import java.util.UUID;

@Repository
public interface GibddRequestRepository extends JpaRepository<GibddRequest, UUID> {
}
