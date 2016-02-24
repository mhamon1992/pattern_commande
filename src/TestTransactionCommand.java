/**
 * Created by maël on 24/02/2016.
 */
import java.util.*;
final class CommandReceiver {
    private int[] c;
    private CommandArgument a;
    private CommandReceiver(){
        c = new int[2];
    }
    private static CommandReceiver cr = new CommandReceiver();
    public static CommandReceiver getHandle() {
        return cr;
    }
    public void setCommandArgument(CommandArgument a) {
        this.a = a;
    }
    public void methAdd() {
        c = a.getArguments();
        System.out.println("The result is " + (c[0]+c[1]));
    }
    public void methSubtract() {
        c = a.getArguments();
        System.out.println("The result is " + (c[0]-c[1]));
    }
}
class CommandManager {
    private Command myCommand;
    public CommandManager(Command  myCommand) {
        this.myCommand  =  myCommand ;
    }
    public void runCommands( ) {
        myCommand.execute();
    }
}
class TransactionCommand implements Command {
    private CommandReceiver commandreceiver;
    private Vector commandnamelist,commandargumentlist;
    private String commandname;
    private CommandArgument commandargument;
    private Command command;
    public TransactionCommand () {
        this(null,null);
    }
    public TransactionCommand ( Vector  commandnamelist, Vector
            commandargumentlist){
        this.commandnamelist = commandnamelist;
        this.commandargumentlist = commandargumentlist;
        commandreceiver =  CommandReceiver.getHandle();
    }
    public void execute( ) {
        for (int i = 0; i < commandnamelist.size(); i++) {
            commandname = (String)(commandnamelist.get(i));
            commandargument = (CommandArgument)((commandargumentlist.get(i)));
            commandreceiver.setCommandArgument(commandargument);
            String classname = commandname + "Command";
            try {
                Class cls = Class.forName(classname);
                command = (Command) cls.newInstance();
            }
            catch (Throwable e) {
                System.err.println(e);
            }
            command.execute();
        }
    }
}
class AddCommand extends TransactionCommand {
    private CommandReceiver cr;
    public AddCommand () {
        cr = CommandReceiver.getHandle();
    }
    public void execute( ) {
        cr.methAdd();
    }
}
class SubtractCommand extends TransactionCommand {
    private CommandReceiver cr;
    public SubtractCommand () {
        cr = CommandReceiver.getHandle();
    }
    public void execute( ) {
        cr.methSubtract();
    }
}
class CommandArgument {
    private int[] args;
    CommandArgument() {
        args = new int[2];
    }
    public int[] getArguments() {
        return args;
    }
    public void setArgument(int i1, int i2) {
        args[0] = i1; args[1] = i2;
    }
}
public class TestTransactionCommand {
    private  Vector clist,alist;
    public TestTransactionCommand() {
        clist = new Vector();
        alist = new Vector();
    }
    public void clearBuffer(Vector c, Vector a) {
        clist.removeAll(c);
        alist.removeAll(a);
    }
    public Vector getClist() {
        return clist;
    }
    public Vector getAlist() {
        return alist;
    }
    public static void main(String[] args) {
        CommandArgument ca,ca2;
        TestTransactionCommand t = new TestTransactionCommand();
        ca = new CommandArgument();
        ca.setArgument(2,8);
        Vector myclist = t.getClist();
        Vector myalist = t.getAlist();
        myclist.addElement("Add"); myalist.addElement(ca);
        TransactionCommand tc = new TransactionCommand(myclist,myalist);
        CommandManager cm = new CommandManager(tc);
        cm.runCommands();
        t.clearBuffer(myclist,myalist);
        ca2 = new CommandArgument();
        ca2.setArgument(5,7);
        myclist = t.getClist();
        myalist = t.getAlist();
        myclist.addElement("Subtract"); myalist.addElement(ca2);
        myclist.addElement("Add"); myalist.addElement(ca2);
        TransactionCommand tc2 = new TransactionCommand(myclist,myalist);
        CommandManager cm2 = new CommandManager(tc2);
        cm2.runCommands();
    }
}
