# justuswah
###### /java/seedu/address/logic/commands/ReauthenticateCommand.java
``` java
public class ReauthenticateCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reauthenticate";
    public static final String COMMAND_ALIAS = "ra";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": reauthenticates the Account SID, Authentication Token, and Sending Number. "
            + "Parameters: "
            + PREFIX_ACCOUNT_SID + "ACCOUNT_SID "
            + PREFIX_AUTH_TOKEN + "AUTHENTICATION_TOKEN "
            + PREFIX_SENDING_NUMBER + "SENDING_NUMBER. "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ACCOUNT_SID + "ACed7baf2459e41d773a5f9c2232d4d975 "
            + PREFIX_AUTH_TOKEN + "6a26cc5c91ff355ebf48fe019700920b "
            + PREFIX_SENDING_NUMBER + "+12082157763";
    public static final String REAUTHENTICATE_SUCCESS = "Details Reauthenticated";

    private String accountSid;
    private String authenticationToken;
    private String sendingNumber;

    public ReauthenticateCommand(String accountSid, String authenticationToken, String sendingNumber) {
        this.accountSid = accountSid;
        this.authenticationToken = authenticationToken;
        this.sendingNumber = sendingNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        try {
            Twilio.init(accountSid, authenticationToken);
            com.twilio.rest.lookups.v1.PhoneNumber number = com.twilio.rest.lookups.v1.PhoneNumber
                    .fetcher(new com.twilio.type.PhoneNumber(sendingNumber))
                    .setType("carrier")
                    .fetch();
        } catch (com.twilio.exception.ApiException e) {
            logger.log(Level.INFO, "Authentication Attempt Failed");
            return new CommandResult(MESSAGE_REAUTHENTICATION_FAILURE);
        }

        model.reauthenticate(accountSid, authenticationToken, sendingNumber);
        logger.log(Level.WARNING, REAUTHENTICATE_SUCCESS);
        return new CommandResult(REAUTHENTICATE_SUCCESS);
    }

}
```
###### /java/seedu/address/logic/commands/RemoveTagCommand.java
``` java
public class RemoveTagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removeTag";
    public static final String COMMAND_ALIAS = "rt";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the specified tag from all contacts in the AddressBook\n"
            + "Parameters: (TAG_NAME) \n"
            + "Example: " + COMMAND_WORD + " friends";
    private final Tag toRemove;

    public RemoveTagCommand(Tag toRemove) {
        this.toRemove = toRemove;
    }

    /**Searches the entire AddressBook for the {@Code toRemove Tag}
     *Returns either "Tag Removed" or "Tag not found", depending on data
     *
     */

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        if (!model.getAddressBook().getTagList().contains(toRemove)) {
            throw new TagNotFoundException(String.format(MESSAGE_TAG_NOT_FOUND));
        } else {
            model.removeTag(toRemove);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            logger.log(Level.FINE, "Tag " + toRemove.tagName + " Removed");
            return new CommandResult(String.format(MESSAGE_REMOVE_TAG_SUCCESS));
        }
    }


}
```
###### /java/seedu/address/logic/commands/SmsCommand.java
``` java
public class SmsCommand extends Command {

    public static final String COMMAND_WORD = "sms";
    private static String accountSid = "ACed7baf2459e41d773a5f9c2232d4d975";
    private static String authenticationToken = "6a26cc5c91ff355ebf48fe019700920b";
    private static String sendingNumber = "+12082157763";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": sends a phone SMS message to the phone number of the contact identified by the index number\n"
            + "Parameters: INDEX (must be a positive integer), MESSAGE (can be string of any length)\n"
            + "Example: " + COMMAND_WORD + " 1" + " text/hello there";

    private final Index targetIndex;
    private String text = null;

    public SmsCommand(Index targetIndex, String text) {
        this.targetIndex = targetIndex;
        this.text = text;
        Twilio.init(accountSid, authenticationToken);
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            return new CommandResult(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        String receivingNumber = lastShownList.get(targetIndex.getZeroBased()).getPhone().toString();

        try {
            Message message = Message
                    .creator(new com.twilio.type.PhoneNumber("+65" + receivingNumber),
                            new com.twilio.type.PhoneNumber(sendingNumber), text)
                    .create();

        } catch (ApiException ae) {
            logger.log(Level.INFO, MESSAGE_SMS_NUMBER_UNAUTHORIZED);
            return new CommandResult(MESSAGE_SMS_NUMBER_UNAUTHORIZED);
        }

        logger.log(Level.FINE, MESSAGE_SMS_PERSON_SUCCESS);
        return new CommandResult(MESSAGE_SMS_PERSON_SUCCESS + " to " + receivingNumber + ": " + text);

    }

    public static void setAccountParticulars(String initializeAccountSid, String initializeAuthenticationToken,
                                             String initializeSendingNumber) {
        accountSid = initializeAccountSid;
        authenticationToken = initializeAuthenticationToken;
        sendingNumber = initializeSendingNumber;
    }
}
```
###### /java/seedu/address/logic/parser/FindCommandParser.java
``` java
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCommand(new AnyParticularContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### /java/seedu/address/logic/parser/ReauthenticateCommandParser.java
``` java
public class ReauthenticateCommandParser implements Parser<ReauthenticateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReauthenticateCommand
     * and returns a ReauthenticateCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ReauthenticateCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ACCOUNT_SID,
                PREFIX_AUTH_TOKEN, PREFIX_SENDING_NUMBER);

        if (!arePrefixesPresent(argMultimap, PREFIX_ACCOUNT_SID, PREFIX_AUTH_TOKEN, PREFIX_SENDING_NUMBER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ReauthenticateCommand.MESSAGE_USAGE));
        }

        try {
            String accountSid = ParserUtil.parseAuthenticator(argMultimap.getValue(PREFIX_ACCOUNT_SID).get());
            String authenticationToken = ParserUtil.parseAuthenticator(argMultimap.getValue(PREFIX_AUTH_TOKEN).get());
            String sendingNumber = ParserUtil.parseAuthenticator(argMultimap.getValue(PREFIX_SENDING_NUMBER).get());
            return new ReauthenticateCommand(accountSid, authenticationToken, sendingNumber);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReauthenticateCommand.MESSAGE_USAGE));
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/logic/parser/RemoveTagCommandParser.java
``` java
public class RemoveTagCommandParser implements Parser<RemoveTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemoveTagCommand
     * and returns a RemoveTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemoveTagCommand parse(String args) throws ParseException {
        try {
            Tag toRemove = new Tag (args);
            return new RemoveTagCommand(toRemove);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/SmsCommandParser.java
``` java
public class SmsCommandParser implements Parser<SmsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SMSCommand
     * and returns an SMSCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SmsCommand parse(String args) throws ParseException {
        Index index;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SMS_TEXT);

        if (!arePrefixesPresent(argMultimap, PREFIX_SMS_TEXT)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(firstWord(args));

        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SmsCommand.MESSAGE_USAGE));
        }

        String text = argMultimap.getAllValues(PREFIX_SMS_TEXT).toString();
        return new SmsCommand(index, text);

    }

    public static String firstWord(String input) {
        return input.split(" ")[1]; // Create array of words and return the 1st word
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}



```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Deletes the tag argument from this AddressBook
     */

    public void deleteTag(Tag toRemove) {
        tags.removeTag(toRemove);
        for (Person person :persons) {
            if (person.getTags().contains(toRemove)) {
                person.remove(toRemove);
            }
        }
    }

    public void reauthenticate (String newId, String newToken, String newNumber) {
        SmsCommand.setAccountParticulars(newId, newToken, newNumber);
    }

    public void smsContact (Index index, String text) {
        new SmsCommand(index, text);
    }

```
###### /java/seedu/address/model/tag/exceptions/TagNotFoundException.java
``` java
public class TagNotFoundException extends CommandException {
    public TagNotFoundException(String message) {
        super(message);
    }
}
```
###### /java/seedu/address/ui/CommandBox.java
``` java
    /**
     * Creates a list of Matched Suggestions based on User Input
     */

    public void createSuggestions() {
        commandTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String userInput = commandTextField.getText();
            List<String> matchedSuggestions = listedSuggestions.stream()
                .filter(i-> i.toLowerCase().startsWith(userInput.toLowerCase()))
                .collect(Collectors.toList());

            if (matchedSuggestions.contains(userInput) || userInput.isEmpty() || matchedSuggestions.isEmpty()) {
                suggestionsMenu.hide();
            } else {
                createPopupWindow(matchedSuggestions);
                if (!suggestionsMenu.isShowing()) {
                    suggestionsMenu.show(this.commandTextField, Side.TOP, 200, 0); // Popup position.
                }
            }
        });
    }

    /**
    * Instantiates the Suggestions Popup Window based on the list {@code listedSuggestions}.
    *
    */

    private void createPopupWindow(List<String> matchedSuggestions) {
        List<CustomMenuItem> popupMenu = new ArrayList<>();
        suggestionsMenu.getItems().clear();
        for (int i = 0; i < matchedSuggestions.size(); i++) {
            Label suggestion = new Label(matchedSuggestions.get(i));
            suggestion.setPrefHeight(30);
            CustomMenuItem item = new CustomMenuItem(suggestion, true);
            popupMenu.add(item);

            item.setOnAction(actionEvent -> {
                logger.log(Level.INFO, suggestion.getText());
                suggestionsMenu.hide();
                commandTextField.setText(suggestion.getText());
                commandTextField.positionCaret(suggestion.getText().length());
            });
        }
        suggestionsMenu.getItems().addAll(popupMenu);
    }
}
```
