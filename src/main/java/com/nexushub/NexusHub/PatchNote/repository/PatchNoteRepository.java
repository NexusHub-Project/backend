package com.nexushub.NexusHub.PatchNote.repository;

import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {
}
