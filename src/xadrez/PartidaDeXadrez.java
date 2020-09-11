package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pecas.xadrez.Rei;
import pecas.xadrez.Torre;
import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private static boolean xequeMate;
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

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

	public static boolean getXequeMate() {
		return xequeMate;
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
		Peca pecaCaturada = movimentandoPeca(origem, destino);
		if (testeXequeMate(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCaturada);
			throw new ExecoesDoXadrez("Voce nao pode se colocar em xequemate");
		}
		xequeMate = (testeXequeMate(oponente(jogadorAtual))) ? true : false;
		proximoTurno();
		return (PecaDeXadrez) pecaCaturada;

	}

	private Peca movimentandoPeca(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		Peca p = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);

		}
	}

	private void validandoPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temPecaNaPosicao(posicao)) {

			throw new ExecoesDoXadrez("Nao Existe peca na posicao de origem.");
		}
		if (jogadorAtual != ((PecaDeXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new ExecoesDoXadrez("A peca escolhida nao e sua");
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

	private Cor oponente(Cor cor) {
		return (cor == Cor.Branco) ? Cor.Preto : Cor.Branco;
	}

	private PecaDeXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não existe o rei " + cor + " no tabuleiro ");
	}

	private boolean testeXequeMate(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecaAdversario = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecaAdversario) {
			boolean[][] mat = p.possiveisMovimentos();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}

		return false;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
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
