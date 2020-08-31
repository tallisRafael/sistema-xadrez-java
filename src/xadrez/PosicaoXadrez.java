package xadrez;

import tabuleiro.Posicao;

public class PosicaoXadrez {
	private char coluna;
	private int linha;

	public char getColuna() {
		return coluna;
	}

	public PosicaoXadrez(char coluna, int linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new ExecoesDoXadrez("Erro na instancia��o das posi�oes do jogo. Valores valido � de a1 ate h8.");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public int getLinha() {
		return linha;
	}

	protected Posicao toPosicao() {
		return new Posicao(8 - linha, coluna - 'a');
	}

	protected static PosicaoXadrez ParaPosicao(Posicao posicao) {
		return new PosicaoXadrez((char) ('a' - posicao.getColuna()), 8 - posicao.getLinha());
	}

	public String toString() {
        return "" + coluna + linha;
	}

}
