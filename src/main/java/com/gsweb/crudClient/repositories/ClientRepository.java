package com.gsweb.crudClient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsweb.crudClient.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
