package com.kn.todosapp.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoJpaController {
	
	private TodoRepository todoRepository;
	
	public TodoJpaController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}
	
	@GetMapping("/users/{username}/todos")
	public List<Todo> retrieveTodos(@PathVariable String username) {
		return todoRepository.findByUsername(username);
	}
	
	@GetMapping("/users/{username}/todos/{id}")
	public Todo retrieveTodos(@PathVariable String username, @PathVariable int id) {
		return todoRepository.findById(id).get();
	}
	
	@DeleteMapping("/users/{username}/todos/{id}")
	public ResponseEntity<Void> deleteTodo(@PathVariable String username,
			@PathVariable int id) {
		todoRepository.deleteById(id);
		return ResponseEntity.noContent().build(); //return 204 status
	}
	
	@PutMapping("/users/{username}/todos/{id}")
	public Todo updateTodo(@PathVariable String username,
			@PathVariable int id, @RequestBody Todo todo) {
		return todoRepository.save(todo);
	}
	
	@PostMapping("/users/{username}/todos")
	public Todo createTodo(@PathVariable String username,
			 @RequestBody Todo todo) {
		todo.setUsername(username);
		todo.setId(null);
		return todoRepository.save(todo);
	}
	
	@GetMapping(path = "/basicauth")
	public String basicAuthCheck() {
		return "Success"; 
	}

}
