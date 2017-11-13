# mzxc152
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
        @Override
        public void sortList(String toSort) {
            fail("There is no person in Contags to sort.");
        }
```
###### /java/seedu/address/logic/commands/SortCommandTest.java
``` java

public class SortCommandTest {
    private static final String NAME_SORT = "name";
    private static final String EMAIL_SORT = "email";
    private static final String PHONE_SORT = "phone";
    private static final String ADDRESS_SORT = "address";
    private static final String TAG_SORT = "tag";

    private Model model;
    private Model expectedModel;


    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void executeNameSortSuccess() {
        SortCommand sortCommand = prepareCommand(NAME_SORT);
        expectedModel.sortList(NAME_SORT);
        assertCommandSuccess(sortCommand, model, sortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeEmailSortSuccess() {
        SortCommand sortCommand = prepareCommand(EMAIL_SORT);
        expectedModel.sortList(EMAIL_SORT);
        assertCommandSuccess(sortCommand, model, sortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executePhoneSortSuccess() {
        SortCommand sortCommand = prepareCommand(PHONE_SORT);
        expectedModel.sortList(PHONE_SORT);
        assertCommandSuccess(sortCommand, model, sortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeAddressSortSuccess() {
        SortCommand sortCommand = prepareCommand(ADDRESS_SORT);
        expectedModel.sortList(ADDRESS_SORT);
        assertCommandSuccess(sortCommand, model, sortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeTagSortSuccess() {
        SortCommand sortCommand = prepareCommand(TAG_SORT);
        expectedModel.sortList(TAG_SORT);
        assertCommandSuccess(sortCommand, model, sortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     *  Parses {@code toSort} into a {@code SortCommand}.
     *  This sorts Contags according to the type of attribute parsed into SortCommand.
     */
    private SortCommand prepareCommand(String toSort) {
        SortCommand command = new SortCommand(toSort);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### /java/seedu/address/logic/parser/SortCommandParserTest.java
``` java

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        assertParseFailure(parser, "birthday",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "!@#$`2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
```
###### /java/systemtests/SortCommandSystemTest.java
``` java

public class SortCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_SORT_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);
    private static final String NAME_SORT = "name";
    private static final String PHONE_SORT = "phone";
    private static final String EMAIL_SORT = "email";
    private static final String ADDRESS_SORT = "address";
    private static final String TAG_SORT = "tag";

    @Test
    public void sort() {
        Model model = getModel();

        /* Case: sort by name -> list is sorted alphabetically by name */
        String command = SortCommand.COMMAND_WORD + " " + NAME_SORT;
        String expectedResultMessage = String.format(SortCommand.MESSAGE_SUCCESS, NAME_SORT);
        model.sortList(NAME_SORT);
        assertCommandSuccess(command, expectedResultMessage, model);

        /* Case: sort by phone -> list is sorted numerically by phone */
        command = SortCommand.COMMAND_WORD + " " + PHONE_SORT;
        expectedResultMessage = String.format(SortCommand.MESSAGE_SUCCESS, PHONE_SORT);
        model.sortList(PHONE_SORT);
        assertCommandSuccess(command, expectedResultMessage, model);

        /* Case: sort by email -> list is sorted alphabetically by email */
        command = SortCommand.COMMAND_WORD + " " + EMAIL_SORT;
        expectedResultMessage = String.format(SortCommand.MESSAGE_SUCCESS, EMAIL_SORT);
        model.sortList(EMAIL_SORT);
        assertCommandSuccess(command, expectedResultMessage, model);

        /* Case: sort by address -> list is sorted alphabetically by address */
        command = SortCommand.COMMAND_WORD + " " + ADDRESS_SORT;
        expectedResultMessage = String.format(SortCommand.MESSAGE_SUCCESS, ADDRESS_SORT);
        model.sortList(ADDRESS_SORT);
        assertCommandSuccess(command, expectedResultMessage, model);

        /* Case: sort by tag -> list is sorted alphabetically by tag */
        Model modelBeforeSortPhone = getModel();
        command = SortCommand.COMMAND_WORD + " " + TAG_SORT;
        expectedResultMessage = String.format(SortCommand.MESSAGE_SUCCESS, TAG_SORT);
        model.sortList(TAG_SORT);
        assertCommandSuccess(command, expectedResultMessage, model);

        /* Case: keyword is wrong -> rejected */
        assertCommandFailure("sort birthday", MESSAGE_INVALID_SORT_COMMAND_FORMAT);

    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays the success message of executing sort command
     * and the model related components equal to the current model.
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
