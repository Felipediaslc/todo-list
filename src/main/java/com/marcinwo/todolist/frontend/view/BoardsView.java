package com.marcinwo.todolist.frontend.view;

import com.marcinwo.todolist.app.entity.TasksBoard;
import com.marcinwo.todolist.app.service.TaskBoardService;
import com.marcinwo.todolist.app.service.UserService;
import com.marcinwo.todolist.frontend.component.CreateBoardDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("boards")
public class BoardsView extends VerticalLayout {

    private UserService userService;
    private TaskBoardService taskBoardService;

    private Button createNewBoardButton = new Button("Create New Board", this::onCreateNewBoardButtonClick);
    private CreateBoardDialog dialog = new CreateBoardDialog(this::onSaveButtonClick, this::onCancelButtonClick);

    @Autowired
    public BoardsView(UserService userService, TaskBoardService taskBoardService) {
        this.userService = userService;
        this.taskBoardService = taskBoardService;
        createLayout();
    }

    private void createLayout() {
        add(createNewBoardButton);

        dialog.addDetachListener(detachEvent -> dialog.clear());

//        dialog.addDialogCloseActionListener(detachEvent -> dialog.clear());
    }

    private void onCreateNewBoardButtonClick(ClickEvent<Button> e) {
        dialog.setDbUsers(userService.findAllWithoutCurrentUser());
        dialog.setCurrentUser(userService.findCurrentUser());
        dialog.open();
    }

    private void onSaveButtonClick() {
        dialog.getBinder().validate();
        if (dialog.getBinder().isValid()) {
            TasksBoard taskBoard = dialog.getTaskBoard();
            taskBoardService.save(taskBoard);
            dialog.close();
            dialog.clear();
            Notification.show("New TaskBoard saved");
        } else {
            Notification.show("Correct data");
        }

    }

    private void onCancelButtonClick() {
        dialog.clear();
        dialog.close();
    }

}
