package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jb.db_spring.domain.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
