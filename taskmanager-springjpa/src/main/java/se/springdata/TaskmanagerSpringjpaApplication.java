package se.springdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

//import se.springdata.model.Status;
//import se.springdata.model.Workitem;
//import se.springdata.repository.WorkitemRepository;
//import se.springdata.service.WorkitemService;

//import se.springdata.model.Issue;
//import se.springdata.model.Status;
//import se.springdata.model.Team;
//import se.springdata.model.User;
//import se.springdata.model.Workitem;
//import se.springdata.repository.IssueRepository;
//import se.springdata.repository.UserRepository;
//import se.springdata.service.IssueService;
//import se.springdata.service.TeamService;
//import se.springdata.service.UserService;
//import se.springdata.service.WorkitemService;

@SpringBootApplication
public class TaskmanagerSpringjpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerSpringjpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ApplicationContext context) {
		return args -> {
			// TeamService service = context.getBean(TeamService.class);
			// UserService service = context.getBean(UserService.class);
			// WorkitemRepository service =
			// context.getBean(WorkitemRepository.class);
			// IssueRepository service = context.getBean(IssueRepository.class);
			// System.out.println(service.findWorkitemStatusById(20L));
			// System.out.println(service.findWorkitemStatusById(20L));
			// WorkitemRepository team =
			// context.getBean(WorkitemRepository.class);
			// System.out.println(team.getDescriptions());

			// Add Team
			// Team team01 = service.addTeam(new Team("TeamOne","active"));
			// Team team02 = service.addTeam(new Team("TeamTwo","active"));
			// Team team03 = service.addTeam(new Team("TeamThree","active"));
			// Team team04 = service.addTeam(new Team("TeamFour","active"));
			// Team team05 = service.addTeam(new Team("TeamFive","active"));

			// Add User.
			// service.addUser(new User("Barack","Obama","e113456789","active",
			// "H-04"));
			// service.addUser(new
			// User("Hillary","Clinton","e123456789","active", "A-01"));
			// service.addUser(new User("Donald","Trump","e123456780","active",
			// "A-02"));
			// service.addUser(new
			// User("Homer","Simpson","e223456789","active","C-01"));
			// service.addUser(new
			// User("Luke","Skywalker","e123456489","active","D-01"));
			// service.addUser(new
			// User("Darth","Vader","e123356789","active","E-01"));
			// service.addUser(new
			// User("Kalle","Anka","e123426789","active","F-01"));
			// service.addUser(new
			// User("Musse","Pigg","e123459789","active","G-01"));
			// service.addUser(new
			// User("Steven","Jobs","e123400789","active","H-01"));
			// service.addUser(new User("Kermit","The
			// Frog","e126476789","active","I-01"));
			// service.addUser(new
			// User("Anders","Andersson","e123866789","active","J-01"));

			// Add Workitem
			// service.addWorkItem(new Workitem("Baking","Making a birthday
			// cake",Status.STARTED));
			// service.addWorkItem(new Workitem("Cooking","Making
			// dinner",Status.UNSTARTED));
			// service.addWorkItem(new Workitem("Reading","Read Harry
			// Potter",Status.STARTED));
			// service.addWorkItem(new Workitem("Movie","Rent Find
			// Nemo",Status.UNSTARTED));
			// service.addWorkItem(new Workitem("Gardning","Plant a
			// tree",Status.STARTED));

			// Add Issue
			// service.addAndAssignIssue(new Issue("Buy the ingrediens from the
			// store today"), 17l);
			// service.addAndAssignIssue(new Issue("Finnish the book"), 19l);
			// service.addAndAssignIssue(new Issue("Make sure you rent it with
			// swedish subtitle"), 20l);

			// System.out.println(service.getAllUserForATeam(1l));

			// System.out.println(service.getAllWorkItemByIssue());

			// System.out.println(service.getAllWorkItemByIssue());
			// service.deleteWorkitem(24l, 20l);

			// service.updateTeam("TeamOne", 3l);
		};

	}
}