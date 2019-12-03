package br.com.thiago.notes.controller

import br.com.thiago.notes.model.Note
import br.com.thiago.notes.repository.NoteRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("notes")
class NoteController (
        private val noteRepository: NoteRepository) {

    @Value("\${server.name}")
    private lateinit var serverName: String
    private val logger: Logger = LoggerFactory.getLogger(NoteController::class.java)

    @GetMapping
    fun list(): List<Note> {
        logger.info("Listando notes no server: $serverName")
        return noteRepository.findAll().toList()
    }

    @PostMapping
    fun add(@RequestBody note: Note): Note {
        logger.info("Adicionando notes no server: $serverName")
        note.server = serverName
        return noteRepository.save(note)
    }

    @PutMapping("{id}")
    fun alter(@PathVariable id: Long, @RequestBody note: Note): Note {
        if (noteRepository.existsById(id)){
            val safeNote = note.copy(id = id)
            return noteRepository.save(safeNote)
        }
        logger.info("Alterando notes no server: $serverName")
        return Note()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Note> {
        logger.info("Deletando notes no server: $serverName")
        return if (noteRepository.existsById(id)){
            noteRepository.deleteById(id)
            ResponseEntity.ok().build()
        }
        else ResponseEntity.badRequest().build<Note>()

    }
}