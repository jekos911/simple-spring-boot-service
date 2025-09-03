package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jb.db_spring.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
