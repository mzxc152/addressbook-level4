package seedu.address.logic.commands;

import seedu.address.model.person.BirthdayChecker;

//@@author hymss
/**
 * Lists all persons in Contags whose birthday falls on the current day.
 */
public class ListBirthdayCommand extends Command {

    public static final String COMMAND_WORD = "listbirthday";
    public static final String COMMAND_ALIAS = "lb";

    public static final String MESSAGE_SUCCESS = "Listed all contacts whose birthday is today.\n"
            + "Wish them a happy birthday by using the mail or SMS command!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all the persons whose birthday is the current day.\n";

    private BirthdayChecker checker = new BirthdayChecker();

    public ListBirthdayCommand (){

    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(checker);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
