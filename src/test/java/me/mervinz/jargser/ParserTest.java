package me.mervinz.jargser;


import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void parserTest() {
        String args = "test -a valueA --C valueC -v";
        ArgumentParser parser = new ArgumentParser();
        parser.setAppName("TestApp")
                .setAppVersion("1.0.1")
                .addCommand("install", "Install application")
                .addOption("a", "A", "A option for application")
                .addOption(null, "bind", "B option for application")
                .addOption("c", "C", "C option for application")
                .addOption("d", "D", "D option for application", "valueD")
                .addOption("v", "verbose", "Give more output.");

        parser.parse(args.split(" "));
        Assert.assertEquals(parser.parsedOption("A"), "valueA");
        Assert.assertEquals(parser.parsedOption("bind"), null);
        Assert.assertEquals(parser.parsedOption("C"), "valueC");
        Assert.assertEquals(parser.parsedOption("D"), "valueD");
        Assert.assertEquals(parser.parsedCommand(), null);
        Assert.assertEquals(parser.parsedOption("verbose"), "");
        parser.printUsage();
    }
}
