package xadrez;

import pecas.xadrez.Rei;
import pecas.xadrez.Torre;
import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.Branco;
		configuracaoInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public PecaDeXadrez[][] getPecas() {
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoDeOrigem) {
		Posicao posicao = posicaoDeOrigem.toPosicao();
		validandoPosicaoOrigem(posicao);

		return tabuleiro.peca(posicao).possiveisMovimentos();
	}

	public PecaDeXadrez movendoPeca(PosicaoXadrez posicaoDeOrigem, PosicaoXadrez posicaoDeDestino) {
		Posicao origem = posicaoDeOrigem.toPosicao();
		Posicao destino = posicaoDeDestino.toPosicao();
		validandoPosicaoOrigem(origem);
		validandoPosicaoDestino(origem, destino);
		Peca capturarPeca = movimentandoPeca(origem, destino);
		proximoTurno();
		return (PecaDeXadrez) capturarPeca;

	}

	private Peca movimentandoPeca(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, destino);
		return pecaCapturada;
	}

	private void validandoPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temPecaNaPosicao(posicao)) {

			throw new ExecoesDoXadrez("Nao Existe peca na posicao de origem.");
		}
		if (jogadorAtual != ((PecaDeXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new ExecoesDoXadrez("A peca escolhida nao � sua");
		}
		if (!tabuleiro.peca(posicao).existePosicaoParaMover()) {
			throw new ExecoesDoXadrez("Nao e possivel mover para essa posicao");
		}
	}

	private void validandoPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new ExecoesDoXadrez("A peca escolhida nao pode se mover para a posicao de destino.");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.Branco) ? Cor.Preto : Cor.Branco;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configuracaoInicial() {
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.Branco));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.Preto));

	}
}
