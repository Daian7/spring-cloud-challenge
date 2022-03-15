package br.com.caelum.eats.pagamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caelum.eats.pagamento.client.PedidoClient;

@Service
public class ClienteRestDoPedido {
    
    @Autowired
    private PedidoClient pedidoClient;
    
	public void notificaPagamentoDoPedido(Long pedidoId) {
    	
    	pedidoClient.atualizaStatus(pedidoId, new PedidoMudancaDeStatusRequest("pago".toUpperCase()));

    }
}




