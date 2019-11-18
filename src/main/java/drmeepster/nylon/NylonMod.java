package drmeepster.nylon;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class NylonMod implements ModInitializer{

	public static final Logger NYLA = LogManager.getLogger("Nylon");

	public static final String[] QUOTES = {
		"1+1 is a metaphor for marriage.",
		"Murder is inconvenient.",
		"When life gives you lemons, make quotes about burning peoples' houses down.",
		"yeet yo problemos away fellow kiddos",
		"DrMeepster is the one true lord.",
		"Books are part of a balanced daily diet."
	};

	public static final String NN_SAYS = "Nylon Nyla says: ";

	public static final String HELP_ME_PLEASE = "Please help me DrMeepster trapped me here to make quotes for you";

	@Override
	public void onInitialize(){
		Random random = new Random();

		if(random.nextInt(10) == 0){
			NYLA.fatal(NN_SAYS + HELP_ME_PLEASE);
		}else{
			NYLA.info(NN_SAYS + QUOTES[random.nextInt(QUOTES.length)]);
		}
	}

}
