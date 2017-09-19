package com.admin.web.util;
import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.NotFoundException;
import com.messagebird.objects.MessageResponse;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.rlax.web.base.MessageBean;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ExampleSendMessage {

    public static boolean exampleSendMessage(String tel,String telExp) {
     /*   if (args.length main< 3) {
            System.out.println("Please specify your access key, one ore more phone numbers and a message body example : java -jar <this jar file> test_accesskey 31612345678,3161112233 \"My message to be send\"");
            return;
        }
*/
        // First create your service object
        String acckey = "oPRr2jnLIyIi8HXtT483aqQX4";
        final MessageBirdService wsr = new MessageBirdServiceImpl(acckey);

        // Add the service to the client
        final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

        try {
            // Get Hlr using msgId and msisdn
            System.out.println("Sending message:");
            final List<BigInteger> phones = new ArrayList<BigInteger>();

            phones.add(new BigInteger("0086"+tel));

            final MessageResponse response = messageBirdClient.sendMessage("MessageBird", telExp, phones);
            System.out.println(response.toString());
        } catch (UnauthorizedException unauthorized) {
            if (unauthorized.getErrors() != null) {
                System.out.println(unauthorized.getErrors().toString());
                return false;
            }
            unauthorized.printStackTrace();
        } catch (GeneralException generalException) {
            if (generalException.getErrors() != null) {
                System.out.println(generalException.getErrors().toString());
                return false;
            }
            generalException.printStackTrace();

        }
        return  true;
    }
}
