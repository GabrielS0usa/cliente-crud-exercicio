package com.gsweb.crudClient.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsweb.crudClient.dto.ClientDTO;
import com.gsweb.crudClient.entities.Client;
import com.gsweb.crudClient.repositories.ClientRepository;
import com.gsweb.crudClient.services.exceptions.DatabaseException;
import com.gsweb.crudClient.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true  )
	public Page<ClientDTO> findAll(Pageable pageable) {
		Page<Client> results = repository.findAll(pageable);
		return results.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true  )
	public ClientDTO findById(Long id) {
		Optional<Client> results = repository.findById(id);
		Client entity = results.orElseThrow(() -> new ResourceNotFoundException("Recuso não encontrado"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long ig, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(ig);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}
	
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha na integridade referencial");
		}
	}
	
	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	}
	
}
