package com.nexushub.NexusHub.PatchNote.service;

import com.nexushub.NexusHub.Exception.Normal.CannotFoundPatchNote;
import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import com.nexushub.NexusHub.PatchNote.dto.PatchNoteDto;
import com.nexushub.NexusHub.PatchNote.repository.PatchNoteRepository;
import com.nexushub.NexusHub.User.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PatchNoteService {
    private final PatchNoteRepository patchNoteRepository;

    public PatchNote save(PatchNoteDto.PatchNoteRequest dto, User author){
        return patchNoteRepository.save(PatchNote.of(dto.getTitle(), dto.getContent(), author));
    }

    public void test(Long id, String title){
        patchNoteRepository.findById(id);
    }

    public Optional<PatchNote> findById(Long id){
        return patchNoteRepository.findById(id);
    }

    public List<PatchNote> findAll(){
        return patchNoteRepository.findAll();
    }

    public PatchNote edit(PatchNoteDto.PatchNoteRequest dto, User author, Long id) throws CannotFoundPatchNote {
        PatchNote patchNote = patchNoteRepository.findById(id).orElseThrow(()-> new CannotFoundPatchNote("해당 패치노트가 없어요"));
        patchNote.update(dto.getTitle(), dto.getContent());
        return patchNoteRepository.save(patchNote);
    }

    public void deleteById(Long id){
        patchNoteRepository.deleteById(id);
    }

    public void addLikeById(Long id){
        findById(id).ifPresent(patchNote -> {patchNote.like();});
    }

    public void addDislikeById(Long id){
        findById(id).ifPresent(patchNote -> {patchNote.dislike();});
    }

    public void addViews(PatchNote patchNote){
        patchNote.view();
    }
}
