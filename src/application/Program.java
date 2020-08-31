package application;

import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();

		while (true) {
			UI.imprimindoTabuleiro(partidaDeXadrez.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez origem = UI.lendoPosicao(sc);

			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lendoPosicao(sc);
			PecaDeXadrez pecaCapturada = partidaDeXadrez.movendoPeca(origem, destino);
		}
	}
}