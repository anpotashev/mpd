package ru.net.arh.mpd.connection.rw;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;
import ru.net.arh.mpd.model.commands.MpdCommand;
import ru.net.arh.mpd.model.commands.MpdMultiCommand;
import ru.net.arh.mpd.model.commands.MpdSingleCommand;
import ru.net.arh.mpd.model.exception.MpdException;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MpdReaderWriterMockImpl implements MpdReaderWriter {
    private Map<String, Map<String, List<String>>> commands = new HashMap<>();

    public void loadYaml(String...fileNames) {
        Arrays.stream(fileNames).forEach(fileName-> {
                Yaml yaml = new Yaml();
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
                commands.putAll(yaml.load(inputStream));
        });
        System.out.println("Here");
    }

    @Override
    public List<String> sendCommand(MpdCommand command) {
        if (command instanceof MpdMultiCommand) {
            Field field = ReflectionUtils.findField(MpdMultiCommand.class, "commands");
            field.setAccessible(true);
            List<MpdSingleCommand> commands = (List<MpdSingleCommand>) ReflectionUtils.getField(field, command);
            List<String> result = commands.stream()
                    .map(this::sendCommand)
                    .flatMap(Collection::stream)
                    .filter(s -> !s.equals("OK"))
                    .collect(Collectors.toList());
            result.add("OK");
            return result;

        }
        String str = command.toString();
        String cmd = str.contains(" ")
                ? str.split(" ")[0]
                : str;
        String params = str.substring(cmd.length()).trim();
        Map<String, List<String>> paramMap = commands.get(cmd);
        if (paramMap == null) {
            throw new MpdException("command not found");
        }
        if (StringUtils.isEmpty(params)) {
            return paramMap.get("default");
        }
        return paramMap.getOrDefault(params, paramMap.get("default"));
    }

    @Override
    public void close() {

    }
}
