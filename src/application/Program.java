package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.ExecoesDoXadrez;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();

		while (true) {
			try {
				UI.limparTela();
				UI.imprimindoTabuleiro(partidaDeXadrez.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lendoPosicao(sc);

				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = UI.lendoPosicao(sc);
				PecaDeXadrez pecaCapturada = partidaDeXadrez.movendoPeca(origem, destino);
			} catch (ExecoesDoXadrez e) {
				System.out.println(e.getMessage());
				sc.hasNextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}