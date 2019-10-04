package com.example.demo;

import com.example.demo.repo.Channel;
import com.example.demo.repo.Event;
import com.example.demo.repo.EventRepo;
import com.example.demo.repo.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Autowired
	private EventRepo eventRepo;

	@Override
	public void run(String... args) throws Exception {

		Template template  = new Template();
		template.setCode("templateCode");
		template.setStatus("active");

		Template template1  = new Template();
		template1.setCode("templateCode1");
		template1.setStatus("active");

		Template template2  = new Template();
		template2.setCode("templateCode2");
		template2.setStatus("active");

		Template template3  = new Template();
		template3.setCode("templateCode3");
		template3.setStatus("active");

		Set<Template> templates = new HashSet<>();
		templates.add(template);
		templates.add(template1);
		templates.add(template2);
		templates.add(template3);
		Channel channel = new Channel();
		channel.setCode("channelcode");

		channel.setTemplate(templates);

		Set<Channel> channels = new HashSet<>();
		channels.add(channel);

		Event incomingEvent = new Event();
		incomingEvent.setCode("eventCode");
		incomingEvent.setChannel(channels);
		//eventRepo.save(incomingEvent);

		Event eventInDb = eventRepo.findById(1l).get();
		Map<String, Channel> channelMapInDb = eventInDb.getChannel().stream().collect(
				Collectors.toMap(Channel::getCode, c -> c));

		eventInDb.setCode(incomingEvent.getCode());

		for(Channel incomingChannel : incomingEvent.getChannel()) {
			Channel channelInDb = channelMapInDb.get(incomingChannel.getCode());

			if(channelInDb != null) {
				Set<Template> templateInDb = channelInDb.getTemplate();
				Set<Template> incomingChannelTemplate = incomingChannel.getTemplate();
				Map<String, Template> templateMapInDbMap = templateInDb.stream().collect(
						Collectors.toMap(Template::getCode, c -> c));

				for (Template incomingT : incomingChannelTemplate) {
					Template templateInDbToUpdate = templateMapInDbMap.get(incomingT.getCode());
					if (templateInDbToUpdate != null) {
						templateInDbToUpdate.setStatus(incomingT.getStatus());
						templateInDbToUpdate.setCode(incomingT.getCode());
					} else {
						channelInDb.getTemplate().add(incomingT);
					}
				}
			} else {
				eventInDb.getChannel().add(incomingChannel);
			}
			eventRepo.save(eventInDb);
		}




	}
}
