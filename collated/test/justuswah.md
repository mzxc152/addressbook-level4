# justuswah
###### /java/seedu/address/logic/commands/FindCommandTest.java
``` java
    @Test
    public void execute_emailKeyword_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("werner@example.com");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ELLE));
    }

    @Test
    public void execute_emailKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("werner@example.com heinz@example.com");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE));
    }

    @Test
    public void execute_addressKeyword_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("little tokyo");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(FIONA));
    }

    @Test
    public void execute_addressKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = prepareCommand("street");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, GEORGE));
    }

    @Test
    public void execute_phoneKeyword_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("98765432");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_phoneKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("98765432 85355255");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_birthdayKeyword_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("01/02/03");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE));
    }

    @Test
    public void execute_birthdayKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("01/02/03 02/03/04");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

```
###### /java/seedu/address/logic/commands/ReauthenticateCommandTest.java
``` java
public class ReauthenticateCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_reauthenticateValidParticulars_success() throws Exception {
        String accountId = "ACed7baf2459e41d773a5f9c2232d4d975";
        String authenticationToken = "6a26cc5c91ff355ebf48fe019700920b";
        String sendingNumber = "+12082157763";
        ReauthenticateCommand reauthenticateCommand = prepareCommand(accountId, authenticationToken, sendingNumber);
        String expectedMessage = REAUTHENTICATE_SUCCESS;
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.reauthenticate(accountId, authenticationToken, sendingNumber);
        assertCommandSuccess(reauthenticateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_rejectInvalidParticulars_success() throws Exception {
        String accountId = "invalid";
        String authenticationToken = "invalid";
        String sendingNumber = "invalid";
        ReauthenticateCommand reauthenticateCommand = prepareCommand(accountId, authenticationToken, sendingNumber);
        String expectedMessage = MESSAGE_REAUTHENTICATION_FAILURE;
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.reauthenticate(accountId, authenticationToken, sendingNumber);
        assertCommandSuccess(reauthenticateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        String firstAccountId = "firstInvalidId";
        String firstAuthenticationToken = "firstAuthenticationToken";
        String firstSendingNumber = "firstSendingNumber";
        String secondAccountId = "secondInvalidId";
        String secondAuthenticationToken = "secondAuthenticationToken";
        String secondSendingNumber = "secondSendingNumber";
        ReauthenticateCommand firstCommand =
                new ReauthenticateCommand(firstAccountId, firstAuthenticationToken, firstSendingNumber);
        ReauthenticateCommand secondCommand =
                new ReauthenticateCommand(secondAccountId, secondAuthenticationToken, secondSendingNumber);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different person -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    /**
     * Returns a {@code ReauthenticateCommand} with the parameter {@code toRemove}.
     */
    private ReauthenticateCommand prepareCommand(String accountId, String authenticationToken, String sendingNumber) {
        ReauthenticateCommand reauthenticateCommand =
                new ReauthenticateCommand(accountId, authenticationToken, sendingNumber);
        reauthenticateCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return reauthenticateCommand;
    }
}
```
###### /java/seedu/address/logic/commands/RemoveTagCommandTest.java
``` java
public class RemoveTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_removeFirstTag_success() throws Exception {
        Tag toRemove = model.getAddressBook().getTagList().get(0);
        RemoveTagCommand removeTagCommand = prepareCommand(toRemove);
        String expectedMessage = String.format(Messages.MESSAGE_REMOVE_TAG_SUCCESS, toRemove);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.removeTag(toRemove);
        assertCommandSuccess(removeTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonExistentTagName_throwsCommandException() throws Exception {
        Tag invalidTag = new Tag ("nonExistentTag");
        RemoveTagCommand removeTagCommand = prepareCommand(invalidTag);
        assertCommandFailure(removeTagCommand, model, String.format(MESSAGE_TAG_NOT_FOUND));
    }

    @Test
    public void execute_removeLastTag_success() throws Exception {
        int lastTagIndex = model.getAddressBook().getTagList().size() - 1;
        Tag toRemove = model.getAddressBook().getTagList().get(lastTagIndex);
        RemoveTagCommand removeTagCommand = prepareCommand(toRemove);
        String expectedMessage = String.format(Messages.MESSAGE_REMOVE_TAG_SUCCESS, toRemove);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.removeTag(toRemove);
        assertCommandSuccess(removeTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        Tag firstTag = model.getAddressBook().getTagList().get(0);
        Tag secondTag = model.getAddressBook().getTagList().get(1);
        RemoveTagCommand removeFirstCommand = new RemoveTagCommand(firstTag);
        RemoveTagCommand removeSecondCommand = new RemoveTagCommand(secondTag);

        // same object -> returns true
        assertTrue(removeFirstCommand.equals(removeFirstCommand));

        // different types -> returns false
        assertFalse(removeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(removeFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(removeFirstCommand.equals(removeSecondCommand));
    }

    /**
     * Returns a {@code RemoveTagCommand} with the parameter {@code toRemove}.
     */
    private RemoveTagCommand prepareCommand(Tag toRemove) {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(toRemove);
        removeTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return removeTagCommand;
    }
}
```
###### /java/seedu/address/logic/commands/SmsCommandTest.java
``` java
public class SmsCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_unauthorizedIndex_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertExecutionFailure(outOfBoundsIndex, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_zeroIndex_failure() {
        Index outOfBoundsIndex = Index.fromZeroBased(0);
        assertExecutionFailure(outOfBoundsIndex, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_numberNotAuthorized_success() throws Exception {
        Index index = Index.fromZeroBased(0);
        String text = "hello";
        SmsCommand smsCommand = prepareCommand(index, text);
        String expectedMessage = MESSAGE_SMS_NUMBER_UNAUTHORIZED;
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.smsContact(index, text);
        assertCommandSuccess(smsCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        Index firstIndex = Index.fromZeroBased(0);
        Index secondIndex = Index.fromZeroBased(1);
        String firstText = "firstMessage";
        String secondText = "secondMessage";

        SmsCommand firstCommand =
                new SmsCommand(firstIndex, firstText);
        SmsCommand secondCommand =
                new SmsCommand(secondIndex, secondText);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different person -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    /**
     * Executes a {@code SmsCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SmsCommand smsCommand = new SmsCommand(index, "hi");

        try {
            smsCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (ApiException ae) {
            assertEquals(expectedMessage, MESSAGE_SMS_NUMBER_UNAUTHORIZED);
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        } catch (CommandException ce) {
            assertEquals(expectedMessage, MESSAGE_SMS_NUMBER_UNAUTHORIZED);
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        } catch (NullPointerException npe) {
            assertEquals(expectedMessage, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code ReauthenticateCommand} with the parameter {@code toRemove}.
     */
    private SmsCommand prepareCommand(Index index, String text) {
        SmsCommand smsCommand =
                new SmsCommand(index, text);
        smsCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return smsCommand;
    }
}
```
###### /java/seedu/address/logic/parser/FindCommandParserTest.java
``` java
    @Test
    public void parse_validArgsAddressField_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new AnyParticularContainsKeywordsPredicate(Arrays.asList("clementi", "bedok")));
        assertParseSuccess(parser, "clementi bedok", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n clementi \n \t bedok  \t", expectedFindCommand);
    }

    @Test
    public void parse_validArgsPhoneField_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new AnyParticularContainsKeywordsPredicate(Arrays.asList("91234567", "98888888")));
        assertParseSuccess(parser, "91234567 98888888", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 91234567 \n \t 98888888  \t", expectedFindCommand);
    }

    @Test
    public void parse_validArgsEmailField_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new AnyParticularContainsKeywordsPredicate(Arrays.asList("alice@example.com",
                        "j@j.com")));
        assertParseSuccess(parser, "alice@example.com j@j.com", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n alice@example.com \n \t j@j.com  \t", expectedFindCommand);
    }

    @Test
    public void parse_validArgsBirthdayField_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new AnyParticularContainsKeywordsPredicate(Arrays.asList("01/01/01", "02/02/02")));
        assertParseSuccess(parser, "01/01/01 02/02/02", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 01/01/01 \n \t 02/02/02  \t", expectedFindCommand);
    }

}
```
###### /java/seedu/address/logic/parser/ReauthenticateCommandParserTest.java
``` java
/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class ReauthenticateCommandParserTest {

    private ReauthenticateCommandParser parser = new ReauthenticateCommandParser();

    @Test
    public void parse_missingNumberArgument_throwsParseException() {
        String missingField = PREFIX_ACCOUNT_SID + " " + PREFIX_AUTH_TOKEN + " ";
        assertParseFailure(parser, missingField,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReauthenticateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingAuthTokenArgument_throwsParseException() {
        String missingField = PREFIX_ACCOUNT_SID + " " + PREFIX_SENDING_NUMBER + " ";
        assertParseFailure(parser, missingField,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReauthenticateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingAccountSid_throwsParseException() {
        String missingField = PREFIX_SENDING_NUMBER + " " + PREFIX_AUTH_TOKEN + " ";
        assertParseFailure(parser, missingField,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReauthenticateCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/parser/RemoveTagCommandParserTest.java
``` java
/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class RemoveTagCommandParserTest {

    private static final String INVALID_TAG = "!invalidTag!";
    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_invalidArgument_throwsParseException() {
        assertParseFailure(parser, INVALID_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
    }

}
```
###### /java/systemtests/FindCommandSystemTest.java
``` java
        /*---------------- Finding contacts based on other fields (not Name) ---------------- */

        /* Case: find person by address -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(4).getAddress();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(4));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person by phone -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(0).getPhone().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(0));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find 2nd person by phone -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(1).getPhone().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(1));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person by email -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(0).getEmail().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(0));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find 2nd person by email -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(2).getEmail().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(2));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find 2nd person by birthday -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(2).getBirthday().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(2));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find 1st person by birthday -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + expectedModel.getAddressBook()
                .getPersonList().get(1).getBirthday().toString();
        ModelHelper.setFilteredList(expectedModel, expectedModel.getAddressBook().getPersonList().get(1));
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find other fields with incomplete words -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " a";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find other fields with another incomplete word -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " b";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
```
###### /java/systemtests/ReauthenticateCommandSystemTest.java
``` java
public class ReauthenticateCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_REAUTHENTICATE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReauthenticateCommand.MESSAGE_USAGE);

    @Test
    public void reauthenticate() {

        /* ----------------- Performing Valid Reauthenticate Operations -------------------- */

        /* Case: Reauthenticates current particulars to dummy values with random whitespace -> Reauthenticate Failure */
        Model expectedModel = getModel();
        String accountSid = "randomSid";
        String authenticationToken = "randomToken";
        String sendingNumber = "+12345678";
        String command = "     " + ReauthenticateCommand.COMMAND_WORD + "     " + PREFIX_ACCOUNT_SID + accountSid + " "
                + PREFIX_AUTH_TOKEN + authenticationToken + " " + PREFIX_SENDING_NUMBER + sendingNumber;
        String expectedResultMessage = MESSAGE_REAUTHENTICATION_FAILURE;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Reauthenticates current particulars to whitespace -> Reauthenticate Failure */
        expectedModel = getModel();
        accountSid = " ";
        authenticationToken = " ";
        sendingNumber = " ";
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_ACCOUNT_SID + accountSid
                + PREFIX_AUTH_TOKEN + authenticationToken + PREFIX_SENDING_NUMBER + sendingNumber;
        expectedResultMessage = MESSAGE_REAUTHENTICATION_FAILURE;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Undoes the previous reauthentication attempt -> Undo Success */
        expectedModel = getModel();
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: redo authentication attempt -> redo success, authentication fails */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Reauthenticates current particulars to whitespace -> Reauthenticate Failure */
        expectedModel = getModel();
        accountSid = " ";
        authenticationToken = " ";
        sendingNumber = " ";
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_ACCOUNT_SID + accountSid
                + PREFIX_AUTH_TOKEN + authenticationToken + PREFIX_SENDING_NUMBER + sendingNumber;
        expectedResultMessage = MESSAGE_REAUTHENTICATION_FAILURE;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Reauthenticates current particulars to actual account -> Reauthenticate Success */
        expectedModel = getModel();
        accountSid = "ACed7baf2459e41d773a5f9c2232d4d975";
        authenticationToken = "6a26cc5c91ff355ebf48fe019700920b";
        sendingNumber = "+12082157763";
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_ACCOUNT_SID + accountSid + " "
                + PREFIX_AUTH_TOKEN + authenticationToken + " " + PREFIX_SENDING_NUMBER + sendingNumber;
        expectedResultMessage = REAUTHENTICATE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Swaps Order of Fields Presented -> Reauthenticate Success */
        expectedModel = getModel();
        accountSid = "ACed7baf2459e41d773a5f9c2232d4d975";
        authenticationToken = "6a26cc5c91ff355ebf48fe019700920b";
        sendingNumber = "+12082157763";
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_SENDING_NUMBER + sendingNumber + " "
                + PREFIX_AUTH_TOKEN + authenticationToken + " " + PREFIX_ACCOUNT_SID + accountSid;
        expectedResultMessage = REAUTHENTICATE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);


        /* ----------------- Performing invalid reauthentication operations -------------------- */

        /* Case: Missing Number Field -> rejected */
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_ACCOUNT_SID + accountSid + " "
                + PREFIX_AUTH_TOKEN + authenticationToken + " ";
        assertCommandFailure(command, MESSAGE_INVALID_REAUTHENTICATE_COMMAND_FORMAT);

        /* Case: Missing Authentication Token Field -> rejected */
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_ACCOUNT_SID + accountSid + " "
                + PREFIX_SENDING_NUMBER + sendingNumber + " ";
        assertCommandFailure(command, MESSAGE_INVALID_REAUTHENTICATE_COMMAND_FORMAT);

        /* Case: Missing Account SID Field -> rejected */
        command = ReauthenticateCommand.COMMAND_WORD + " " + PREFIX_AUTH_TOKEN + authenticationToken + " "
                + PREFIX_SENDING_NUMBER + sendingNumber + " ";
        assertCommandFailure(command, MESSAGE_INVALID_REAUTHENTICATE_COMMAND_FORMAT);

        /* Case: Mixed Case Command Word -> rejected */
        command = "rEaUtHeNtIcATe" + " " + PREFIX_SENDING_NUMBER + sendingNumber + " "
                + PREFIX_AUTH_TOKEN + authenticationToken + " " + PREFIX_ACCOUNT_SID + accountSid;
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged even after tag removal.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
    }

}
```
###### /java/systemtests/RemoveTagCommandSystemTest.java
``` java
public class RemoveTagCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_REMOVE_TAG_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE);

    @Test
    public void removeTag() {

        /* ----------------- Performing removeTag operations with filled list of tags -------------------- */

        /* Case: Deletes the first Tag in the list, with leading and trailing whitespace -> Tag deleted */
        Model expectedModel = getModel();
        Tag toRemove = expectedModel.getAddressBook().getTagList().get(0); //obtaining the first tag, to be removed
        String command = "     " + RemoveTagCommand.COMMAND_WORD + "      "
                + toRemove.getTagName() + "     ";
        Tag deletedTag = removeTag(expectedModel, toRemove);
        String expectedResultMessage = String.format(MESSAGE_REMOVE_TAG_SUCCESS, deletedTag);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Removes last tag in list -> Last Tag removed */
        Model modelBeforeDeletingLast = getModel();
        int lastIndex = expectedModel.getAddressBook().getTagList().size();
        Tag lastTag = expectedModel.getAddressBook().getTagList().get(lastIndex - 1);
        assertCommandSuccess(lastTag);

        /* Case: undo deleting the last person in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last person in the list -> last person deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeTag(modelBeforeDeletingLast, lastTag);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* ----------------- Performing invalid removeTag operations -------------------- */

        /* Case: Nonexistent Tag -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + " nonexistentTag";
        assertCommandFailure(command, MESSAGE_TAG_NOT_FOUND);

        /* Case: Recently deleted Tag -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + " " + lastTag.tagName;
        assertCommandFailure(command, MESSAGE_TAG_NOT_FOUND);

        /* Case: Invalid Argument (Non-alphanumeric) -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + " !!" + lastTag.tagName;
        assertCommandFailure(command, MESSAGE_INVALID_REMOVE_TAG_COMMAND_FORMAT);

        /* Case: Invalid Arguments (extra argument) -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + " " + lastTag.tagName + " extraTag";
        assertCommandFailure(command, MESSAGE_INVALID_REMOVE_TAG_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        command = "rEmOvEtAg" + " " + lastTag.tagName;
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

    }

    /**
     * Removes the specified {@code Tag} in {@code model}'s address book.
     * Returns the deleted Tag
     */
    private Tag removeTag(Model model, Tag toRemove) {
        try {
            model.removeTag(toRemove);
        } catch (TagNotFoundException ive) {
            throw new AssertionError("Tag does not exist.");
        }
        return toRemove;
    }

    /**
     * Deletes the tag at {@code toRemove} by creating a default {@code RemoveTagCommand} using {@code toRemove} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see RemoveTagCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Tag toRemove) {
        Model expectedModel = getModel();
        Tag deletedTag = removeTag(expectedModel, toRemove);
        String expectedResultMessage = String.format(MESSAGE_REMOVE_TAG_SUCCESS, deletedTag);
        assertCommandSuccess(
                RemoveTagCommand.COMMAND_WORD + " " + toRemove.getTagName(),
                expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged even after tag removal.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertStatusBarUnchangedExceptSyncStatus();
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertStatusBarUnchanged();
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
    }
}
```
###### /java/systemtests/SmsCommandSystemTest.java
``` java
public class SmsCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void sms() throws Exception {

        /* ----------------- Performing Valid Reauthenticate Operations -------------------- */

        /* Case: Attempts to use SMS command without Index -> Invalid Command Format */
        String message = "hello";
        String command = SmsCommand.COMMAND_WORD + " " + PREFIX_SMS_TEXT + message;
        String expectedResultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE);
        assertCommandFailure(command, expectedResultMessage);

        /* Case: Attempts to use SMS command with invalid index -> Returns "invalid index" message*/
        Model expectedModel = getModel();
        command = SmsCommand.COMMAND_WORD + " " + expectedModel.getAddressBook().getPersonList().size() + 1 + " "
                + PREFIX_SMS_TEXT + message;
        expectedResultMessage = MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Attempts to use SMS command with unauthorized number -> Returns "Number unauthorized" message*/
        expectedModel = getModel();
        command = SmsCommand.COMMAND_WORD + " " + expectedModel.getAddressBook().getPersonList().size() + " "
                + PREFIX_SMS_TEXT + message;
        expectedResultMessage = MESSAGE_SMS_NUMBER_UNAUTHORIZED;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Attempts to use SMS command with random whitespace, unauthorized number -> rejected */
        expectedModel = getModel();
        command = SmsCommand.COMMAND_WORD + "         " + expectedModel.getAddressBook().getPersonList().size() + "    "
                + PREFIX_SMS_TEXT + message;
        expectedResultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE);
        assertCommandFailure(command, expectedResultMessage);

        /* Case: undo last SMS command -> Undo Fail, SMS Command Not Undoable */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo last SMS command -> redo Fail, SMS Command Not Undoable, so not Redoable */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* ----------------- Performing invalid SMS operations -------------------- */

        /* Case: Nonexistent text message -> rejected */
        command = SmsCommand.COMMAND_WORD + " 1";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE));

        /* Case: text prefix not all small caps -> rejected */
        command = SmsCommand.COMMAND_WORD + " 1 " + "tExt/" + message;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE));

        /* Case: Mixed case command word -> Unknown Command */
        command = "sMs" + " 1 " + PREFIX_SMS_TEXT + message;
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged even after tag removal.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertSelectedCardUnchanged();
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
    }
}

```
