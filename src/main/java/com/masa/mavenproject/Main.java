/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.masa.mavenproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.LoginException;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


/**
 *
 * @author Teuteu
 */
public class Main extends ListenerAdapter
{
    public static void main(String[] args)
    {
        String DiscordBotKey = System.getenv("DISCORDBOTKEY");
        try
        {
            JDA jda = new JDABuilder(DiscordBotKey)         // The token of the account that is logging in.
                    .addEventListeners(new Main())
                    .build();
            jda.awaitReady();
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException | InterruptedException e)
        {
        }
    }
	static Double RedSci = 0.001;
	static Double GreenSci = 0.0030;
	static Double MilSci = 0.0082;
	static Double BlueSci = 0.0227;
	static Double PurpleSci = 0.0978;
	static Double YellowSci = 0.1063;
	static Double WhiteSci = 0.4182;
	
	public int getNbPotionNeeded(Double curr, Double target, Double difficulty, Double SciUsed){
		Double currEvo = curr / 100;
		Double targetEvo = target / 100;
		SciUsed *= difficulty;
		int nbPotionUsed = 0;
		while (currEvo <= targetEvo){
			double e2 = currEvo * 100 + 1;
			double dimModifier = (1/(Math.pow(10.0, e2 * 0.017)))/(e2*0.5);
			double evoGain = SciUsed * dimModifier;
			currEvo = currEvo + evoGain;
			int scale = (int) Math.pow(10, 12);
			currEvo =  (double) Math.round(currEvo * scale) / scale;
			nbPotionUsed++;
		}
		return nbPotionUsed;
	}
	
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        JDA jda = event.getJDA();

        User author = event.getAuthor();
        Message message = event.getMessage(); 
        MessageChannel channel = event.getChannel(); 
        String msg = message.getContentDisplay(); 

        if  (msg.startsWith("!evo"))   //Note, I used "startsWith, not equals.
        {
            Pattern p = Pattern.compile("!evo ([a-z]*[A-Z]*) ([0-9.]+) ([0-9.]+)");
            Matcher m = p.matcher(msg);
            
            if (m.matches()){
                String difficulty =  m.group(1);
                difficulty = difficulty.toUpperCase();
                String currentEvoStr = m.group(2);
                String targetEvoStr = m.group(3);
                Double currentEvo;
                Double targetEvo;
                
                try{
                currentEvo =  Double.parseDouble(currentEvoStr);
                }catch (NumberFormatException e) {
                    channel.sendMessage("Error processing the current evolution you specified : "+currentEvoStr+"(Error message : "+e.getMessage()+")").queue();
                    return;
                } 
                
                try{
                targetEvo =  Double.parseDouble(targetEvoStr);
                }catch (NumberFormatException e) {
                    channel.sendMessage("Error processing the target evolution you specified : "+targetEvoStr+"( Error message : "+e.getMessage()+")").queue();
                    return;
                } 
                
                if (currentEvo >= targetEvo){
                    channel.sendMessage("It doesnt make sense ("+currentEvo+">="+targetEvo+")... Please have a target evolution value higher than the current evolution value").queue();
                    return;
                }
                
                if (!difficulty.equals("PEACEFUL")
                        && !difficulty.equals("PIECEOFCAKE")
                        && !difficulty.equals("EASY")
                        && !difficulty.equals("NORMAL")
                        && !difficulty.equals("HARD")
                        && !difficulty.equals("NIGHTMARE")
                        && !difficulty.equals("INSANE")){
                    channel.sendMessage("Difficulty for !evo must be PEACEFUL or PIECEOFCAKE or EASY or NORMAL or HARD or NIGHTMARE or INSANE.\nExample of command : **!evo HARD 20 51**").queue();
                }
                else{
                    Double difficultyPercentage;
                    switch(difficulty){
                        case "PEACEFUL":
                            difficultyPercentage = 0.25;
                           break;
                        case "PIECEOFCAKE":
                            difficultyPercentage = 0.50;
                           break;
                        case "EASY":
                            difficultyPercentage = 0.75;
                           break;
                        case "NORMAL":
                            difficultyPercentage = 1.0;
                           break;
                        case "HARD":
                            difficultyPercentage = 1.5;
                           break;
                        case "NIGHTMARE":
                            difficultyPercentage = 2.5;
                           break;
                        case "INSANE":
                            difficultyPercentage = 5.0;
                           break;
                        default:
                           channel.sendMessage("Shouldn't happen, did you just break the bot?! Ignore my answer for your evo command !").queue();
                           return;
                    }
                    int Red = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, RedSci);
                    int Green = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, GreenSci);
                    int Mil = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, MilSci);
                    int Blue = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, BlueSci);
                    int Purple = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, PurpleSci);
                    int Yellow = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, YellowSci);
                    int White = getNbPotionNeeded(currentEvo,targetEvo,difficultyPercentage, WhiteSci);

                    String MessageToSend ="";
                    MessageToSend+= "("+difficulty+")Red Science : " + Red + " sci to go from " +currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")Green Science : " + Green + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")Mil Science : " + Mil + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")Blue Science : " + Blue + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")Purple Science : " + Purple + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")Yellow Science : " + Yellow + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    MessageToSend+= "("+difficulty+")White Science : " + White + " sci to go from "+currentEvo+ " to "+ targetEvo;
                    MessageToSend+="\n";
                    channel.sendMessage(MessageToSend).queue();
                }
            }else
            {
                channel.sendMessage("Try again...\nThe syntax is : **!evo DIFFICULTY currentEvolution TargetEvolution**\nExample : **!evo HARD 50 92.5**\nExample of difficulty : PEACEFUL or PIECEOFCAKE or EASY or NORMAL or HARD or NIGHTMARE or INSANE").queue();
            }
        }
    }
}
