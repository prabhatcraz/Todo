package com.todos.manipulator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.todos.dal.TodoRepository;
import com.todos.exceptions.ResourceNotFoundException;
import com.todos.model.Todo;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Manages operations related to stocks.
 */
@Service
public class TodoService {
    private static Logger logger = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    private final Integer pageSize;

    @Autowired
    public TodoService(TodoRepository todoRepository, Integer pageSize) {
        this.todoRepository = todoRepository;
        this.pageSize = pageSize;
    }

    /**
     * Creates a todo if the symbol of the todo does not exist in our database.
     * @param todo to be created.
     * @return Todo created.
     */
    public Todo create(final Todo todo) {
        Preconditions.checkNotNull(todo.getTitle(), "Title of a Todo cannot be null.");

        if(todo.getDueDate() == null) {
            return todoRepository.save(todo.withDueDate(new Date()));
        }

        return todoRepository.save(todo);
    }

    /**
     * Gets a {@link Todo} by its id.
     * @param id
     * @return
     */
    public Todo get(final Long id) {
        final Todo todo = todoRepository.findOne(id);
        final String message = String.format("No todo found with id %s", id);
        return Optional
                .of(todo)
                .orElseThrow(()-> new ResourceNotFoundException(message));
    }

    /**
     * Gets a {@link JSONObject} with information for the page.
     * {
     *      "page": 2,
     *      "items": <<get of stocks>>,
     *      "maxPage": 4
     * }
     * @param pageNumber
     * @return
     */
    public JSONObject get(final int pageNumber) {

        List<Todo> todos = todoRepository.getTodos(new Date(), new Date());

        final JSONObject obj = new JSONObject();

        obj.put("items", ImmutableList.copyOf(todos.iterator()));
        obj.put("page", pageNumber);
        obj.put("maxPage", todos.size());
        return obj;
    }

    /**
     *
     */
    public List<Todo> getTodosByDate(final Date date) {
        return todoRepository.getTodos(atStartOfDay(date), atEndOfDay(date));
    }

    /**
     * Creates todos in bulk. This has been created for testing purposes only.
     * @param todos
     */
    public void bulkCreate(List<Todo> todos) {
        todoRepository.save(todos);
    }

    Date atEndOfDay(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    Date atStartOfDay(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public void delete(final Long id) {
        todoRepository.delete(id);
    }
}