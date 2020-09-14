package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaDeXadrez extends Peca {

	private Cor cor;
	private int quantidadeMovimento;

	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	public int  getQuantidadeMovimento() {
		return quantidadeMovimento;
	}

	public void incrementarQuantidadeMovimento() {
		quantidadeMovimento++;
	}
	
	public void decrementarQuantidadeMovimento() {
		quantidadeMovimento--;
	}

	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.ParaPosicao(posicao);
	}

	protected boolean existePecaInimiga(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}

}