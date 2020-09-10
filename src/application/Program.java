package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.ExecoesDoXadrez;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		List<PecaDeXadrez> capturada = new ArrayList<>();

		while (true) {
			try {
				UI.limparTela();
				UI.imprimindoPartida(partidaDeXadrez, capturada);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lendoPosicao(sc);

				boolean[][] movimentosPossiveis = partidaDeXadrez.possiveisMovimentos(origem);
				UI.limparTela();
				UI.imprimindoTabuleiro(partidaDeXadrez.getPecas(), movimentosPossiveis);
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = UI.lendoPosicao(sc);

				PecaDeXadrez pecaCapturada = partidaDeXadrez.movendoPeca(origem, destino);
				if(pecaCapturada != null) {
					capturada.add(pecaCapturada);
				}

			} catch (ExecoesDoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}