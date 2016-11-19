/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Mervin <mofei2816@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.mervinz.jargser;

import java.util.ArrayList;
import java.util.List;

/**
 * Argument Parser
 *
 * @author Mervin
 */
@SuppressWarnings("all")
public class ArgumentParser {

    // name of app
    private String appName = "app";

    // version of app
    private String appVersion = "1.0.0";

    // parsed command
    private String command = null;

    // supported commands
    private List<Command> commands = new ArrayList<>();

    // supported options
    private List<Option> options = new ArrayList<>();

    /**
     * Setup name of app
     *
     * @param appName name
     * @return instance of parser
     */
    public ArgumentParser setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Setup version of app
     *
     * @param appVersion version
     * @return instance of parser
     */
    public ArgumentParser setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    /**
     * Add command setting to parser
     *
     * @param command command
     * @param desc description
     * @return instance of parser
     */
    public ArgumentParser addCommand(String command, String desc) {
        if (command != null && command.trim().length() != 0) {
            this.commands.add(new Command(command, desc));
        }
        return this;
    }

    private ArgumentParser addOption(Option option) {
        if (option.validate()) {
            for (Option optionItem : options) {
                if (option.getS() != null && option.getS().trim().length() != 0) {
                    if (option.getS().equals(optionItem.getS())) {
                        return this;
                    }
                }
                if (option.getFlag() != null && option.getFlag().trim().length() != 0) {
                    if (option.getFlag().equals(optionItem.getFlag())) {
                        return this;
                    }
                }
            }
            this.options.add(option);
        }
        return this;
    }

    /**
     * Add option setting to parser
     *
     * @param s short flag
     * @param flag flag
     * @param desc description
     * @return instance of parser
     */
    public ArgumentParser addOption(String s, String flag, String desc) {
        return addOption(s, flag, desc, null);
    }

    /**
     * Add option setting to parser with default value
     *
     * @param s short flag
     * @param flag flag
     * @param desc description
     * @param defaultValue default value
     * @return instance of parser
     */
    public ArgumentParser addOption(String s, String flag, String desc, String defaultValue) {
        return this.addOption(new Option(s, flag, desc, defaultValue));
    }

    /**
     * Parse command and option from argument string array
     *
     * @param args array
     */
    public void parse(String[] args) {

        if (args.length == 0) {
            return;
        }

        int index = 0;
        // parse command
        if (!args[0].startsWith("-")) {
            for (Command command : this.commands) {
                if (command.getCommand().equals(args[0])) {
                    this.command = args[0];
                    index++;
                    break;
                }
            }
        }

        // parse option
        while (index < args.length) {
            if (args[index].startsWith("-")) {
                if (index + 1 >= args.length || args[index + 1].startsWith("-")) {
                    index++;
                    continue;
                }
                Option option = null;
                String value = args[index + 1];
                if (args[index].startsWith("--")) {
                    // flag
                    String flag = args[index].trim().replaceAll("-", "");
                    for (Option optionItem : this.options) {
                        if (optionItem.getFlag() != null && optionItem.getFlag().equals(flag)) {
                            optionItem.setValue(value);
                        }
                    }
                } else {
                    // short flag
                    String shortFlag = args[index].trim().replaceAll("-", "");
                    for (Option optionItem : this.options) {
                        if (optionItem.getS() != null && optionItem.getS().equals(shortFlag)) {
                            optionItem.setValue(value);
                        }
                    }
                }
            }
            index++;
        }
    }

    /**
     * Return value of specified flag.
     *
     * @param flag argument flag
     * @return value
     */
    public String parsedOption(String flag) {
        for (Option option : this.options) {
            if (option.getFlag().equals(flag)) {
                if (option.getValue() == null) {
                    return option.getDefaultValue();
                }
                return option.getValue();
            }
        }
        return null;
    }

    /**
     * Return command which parsed by Parser
     *
     * @return command
     */
    public String parsedCommand() {
        return command;
    }

    /**
     * Print useage info
     */
    public void printUsage() {
        System.out.println(usage());
    }

    /**
     * Generate usage info
     *
     * @return string include usage info
     */
    public String usage() {
        // Usage title
        StringBuilder usageBuilder =
                new StringBuilder(String.format("%s %s\n\nUsage:\n", this.appName, this.appVersion));
        usageBuilder.append(String.format("  %s <command> [options]\n", this.appName));

        // Commands
        if (this.commands.size() != 0) {
            usageBuilder.append("\nCommands:\n");
            for (Command command : this.commands) {
                usageBuilder.append("  ");
                usageBuilder.append(command.getCommand());
                int words = command.getCommand().length();
                for (int i = 0; i < 28 - words; i++) {
                    usageBuilder.append(" ");
                }
                if (command.getDesc() != null) {
                    usageBuilder.append(command.getDesc());
                }
                usageBuilder.append("\n");
            }
        }

        // Options
        if (this.options.size() != 0) {
            usageBuilder.append("\nOptions:\n");
            for (Option option : this.options) {
                int words = 0;
                usageBuilder.append("  ");
                if (option.getS() != null) {
                    usageBuilder.append(String.format("-%s  ", option.getS()));
                    words += option.getS().length() + 3;
                }
                if (option.getFlag() != null) {
                    usageBuilder.append(String.format("--%s", option.getFlag()));
                    words += option.getFlag().length() + 2;
                }
                for (int i = 0; i < 28 - words; i++) {
                    usageBuilder.append(" ");
                }
                usageBuilder.append(String.format("%s\n", option.getDesc()));
            }
        }
        return usageBuilder.toString();
    }

}

/**
 * Command for Argument Parser
 *
 * @author Mervin
 */
@SuppressWarnings("all")
class Command {

    private String command;

    private String desc;

    Command(String command, String desc) {
        this.command = command;
        this.desc = desc;
    }

    String getCommand() {
        return command;
    }

    Command setCommand(String command) {
        this.command = command;
        return this;
    }

    String getDesc() {
        return desc;
    }

    Command setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}

/**
 * Option for Argument Parser
 *
 * @author Mervin
 */
@SuppressWarnings("all")
class Option {

    private String s = null;

    private String flag = null;

    private String desc = null;

    private String defaultValue = null;

    private String value = null;

    Option(String s, String flag, String desc, String defaultValue) {
        this.s = s;
        this.flag = flag;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    String getS() {
        return s;
    }

    Option setS(String s) {
        this.s = s;
        return this;
    }

    String getFlag() {
        return flag;
    }

    Option setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    String getDesc() {
        return desc;
    }

    Option setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    String getDefaultValue() {
        return defaultValue;
    }

    Option setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    String getValue() {
        return value;
    }

    Option setValue(String value) {
        this.value = value;
        return this;
    }

    boolean validate() {
        boolean validFlag = this.flag != null && this.flag.trim().length() != 0;
        boolean validDesc = this.desc != null && this.desc.trim().length() != 0;
        return validFlag && validDesc;
    }
}
