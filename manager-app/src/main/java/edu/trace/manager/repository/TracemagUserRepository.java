package edu.trace.manager.repository;

import edu.trace.manager.entity.TracemagUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TracemagUserRepository extends CrudRepository<TracemagUser, Integer> {
    Optional<TracemagUser> findByUsername(String username);
}
